package com.wanologicalsolutions.apps.bankadmin.repository

import android.content.Context
import com.wanologicalsolutions.apps.bankadmin.db.BankAppDatabase
import com.wanologicalsolutions.apps.bankadmin.repository.datasource.ApiDataSource
import com.wanologicalsolutions.apps.bankadmin.repository.datasource.LocalSearchDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideTripsConDatabase(@ApplicationContext appContext: Context) =
        BankAppDatabase.invoke(appContext)

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideApiDataSource(apiService: ApiService) = ApiDataSource(apiService)

    @Singleton
    @Provides
    fun provideSearchLocalDataSource(database: BankAppDatabase) =
        LocalSearchDataSource(database)

    @Singleton
    @Provides
    fun provideApiRepository(
        apiDataSource: ApiDataSource,
        localSearchDataSource: LocalSearchDataSource
    ) = ApiRepository(apiDataSource, localSearchDataSource)
}