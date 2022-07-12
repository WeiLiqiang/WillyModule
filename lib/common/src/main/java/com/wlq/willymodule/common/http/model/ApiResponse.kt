package com.wlq.willymodule.common.http.model

import com.squareup.moshi.Json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.Serializable

data class ApiResponse<T>(val errorCode: Int, val errorMsg: String, val data: T) : Serializable

data class ApiPageResponse<T>(
    @Json(name = "total") val total: Int,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "curPage") val curPage: Int,
    @Json(name = "offset") val offset: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "over") val over: Boolean,
    @Json(name = "datas") var datas: List<T>
) : Serializable

data class ApiException(val errorCode: Int, val errorMsg: String, val originMsg: String) : Serializable

suspend fun <T> ApiResponse<T>.executeResponse(
    successBlock: (suspend CoroutineScope.() -> Unit)? = null,
    errorBlock: (suspend CoroutineScope.() -> Unit)? = null
): HttpResult<T> {
    return coroutineScope {
        if (errorCode == -1) {
            errorBlock?.let { it() }
            HttpResult.Error(ApiException(errorCode, errorMsg, ""))
        } else {
            successBlock?.let { it() }
            HttpResult.Success(data)
        }
    }
}

suspend fun <T : Any> ApiResponse<T>.doSuccess(successBlock: (suspend CoroutineScope.(T) -> Unit)? = null): ApiResponse<T> {
    return coroutineScope {
        if (errorCode != -1) successBlock?.invoke(this, this@doSuccess.data)
        this@doSuccess
    }

}

suspend fun <T : Any> ApiResponse<T>.doError(errorBlock: (suspend CoroutineScope.(String) -> Unit)? = null): ApiResponse<T> {
    return coroutineScope {
        if (errorCode == -1) errorBlock?.invoke(this, this@doError.errorMsg)
        this@doError
    }
}