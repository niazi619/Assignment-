package com.wanologicalsolutions.apps.bankadmin.network.enums

data class Response<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Response<T> =
            Response(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T? = null, message: String): Response<T> =
            Response(status = Status.ERROR, data = data, message = message)

        fun <T> loading(): Response<T> =
            Response(status = Status.LOADING, data = null, message = null)
    }
}