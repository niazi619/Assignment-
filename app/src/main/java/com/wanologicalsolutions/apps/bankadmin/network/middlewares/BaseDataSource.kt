package com.wanologicalsolutions.apps.bankadmin.network.middlewares

import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import com.wanologicalsolutions.apps.bankadmin.network.enums.Response
import com.wanologicalsolutions.apps.bankadmin.network.exceptions.*
import com.wanologicalsolutions.apps.bankadmin.network.responsebodies.ApiResponse
import com.wanologicalsolutions.apps.bankadmin.network.exceptions.InternalServerException
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> retrofit2.Response<T>): Response<T> {
        val response: retrofit2.Response<T>
        try {
            response = call()
        } catch (e: MalformedJsonException) {
            if (!CONSTANTS.CONFIG.isProductionMode()) e.printStackTrace()
            return error(InternalServerException().message)
        } catch (t: Throwable) {
            if (!CONSTANTS.CONFIG.isProductionMode()) t.printStackTrace()
            return error(mapToNetworkError(t).message!!)
        }
        when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) return Response.success(body)
            }
            else -> {
                val errorBody = response.errorBody()
                val errorResponse =
                    Gson().fromJson(errorBody?.charStream(), ApiResponse::class.java)
                return when {
                    errorResponse != null -> {
                        error(errorResponse.message ?: mapApiException(response.code()).message!!)
                    }
                    errorBody != null -> {
                        error(mapApiException(response.code()).message!!)
                    }
                    else -> {
                        error(mapApiException(0).message!!)
                    }
                }
            }
        }
        return error(HttpException(response).message!!)
    }

    private fun mapApiException(code: Int): Exception {
        return when (code) {
            HttpURLConnection.HTTP_NOT_FOUND -> NotFoundException()
            HttpURLConnection.HTTP_UNAUTHORIZED -> UnAuthorizedException()
            HttpURLConnection.HTTP_INTERNAL_ERROR -> InternalServerException()
            else -> UnKnownException()
        }
    }

    private fun mapToNetworkError(t: Throwable): Exception {
        return when (t) {
            is SocketTimeoutException -> SocketTimeoutException("Connection Timed Out")
            is ConnectException -> NoInternetException()
            is UnknownHostException -> Exception("Server not responding")
            else -> UnKnownException()
        }
    }

    private fun <T> error(message: String): Response<T> {
        if (!CONSTANTS.CONFIG.isProductionMode()) Timber.d(message)
        // CONSTANTS.NETWORK.SOMETHING_WENT_WRONG.format(message)
        return Response.error(message = message)
    }

}


fun String.toThrow(): Throwable = Throwable(this)