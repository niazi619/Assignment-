package com.wanologicalsolutions.apps.bankadmin.network.middlewares

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.wanologicalsolutions.apps.bankadmin.network.enums.Response
import com.wanologicalsolutions.apps.bankadmin.network.enums.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun <T> processResponse(
    networkCall: suspend () -> Response<T>
): LiveData<Response<T>> =
    liveData(Dispatchers.IO) {
        emit(Response.loading())
        val responseStatus = networkCall.invoke()
        when (responseStatus.status) {
            Status.SUCCESS -> {
                emit(Response.success(responseStatus.data!!))
            }
            else -> {
                emit(Response.error(data = null, message = responseStatus.message!!))
            }
        }
    }

fun <T> flowResponse(
    networkCall: suspend () -> Response<T>
): Flow<Response<T>> =
    flow {
        emit(Response.loading())
        val responseStatus = networkCall.invoke()
        when (responseStatus.status) {
            Status.SUCCESS -> {
                emit(Response.success(responseStatus.data!!))
            }
            else -> {
                emit(Response.error(data = null, message = responseStatus.message!!))
            }
        }
    }.catch {
        emit(Response.error(data = null, message = it.message!!))
    }.flowOn(Dispatchers.IO)