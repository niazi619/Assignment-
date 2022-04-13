package com.wanologicalsolutions.apps.bankadmin.di.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import com.wanologicalsolutions.apps.bankadmin.helpers.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {

        if (!CONSTANTS.CONFIG.isProductionMode()) Timber.plant(Timber.DebugTree())
        val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
            if (!CONSTANTS.CONFIG.isProductionMode()) Timber.i(message)
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val user = runBlocking { SessionManager.user.firstOrNull() }
                val isLogin = runBlocking { SessionManager.isLoginFlow.firstOrNull() } ?: false
                if (!CONSTANTS.CONFIG.isProductionMode()) Timber.i("Authorization: Bearer ${user?.accessToken}")
                chain.proceed(
                    when (isLogin) {
                        true -> chain.request()
                            .newBuilder()
                            .addHeader(
                                "Authorization",
                                "Bearer ${user?.accessToken}"
                            )
                            .build()
                        else -> chain.request()
                            .newBuilder()
                            .build()
                    }
                )
            }
            .readTimeout(CONSTANTS.NETWORK.READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONSTANTS.NETWORK.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(CONSTANTS.SERVER.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

}