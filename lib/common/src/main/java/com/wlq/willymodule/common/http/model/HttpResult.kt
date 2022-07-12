package com.wlq.willymodule.common.http.model

sealed class HttpResult<out T> {

    data class Success<out T>(val data: T) : HttpResult<T>()

    data class Error(val apiException: ApiException) : HttpResult<Nothing>()

    override fun toString(): String {

        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=${apiException.errorMsg}]"
        }
    }
}