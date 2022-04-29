package com.wlq.willymodule.common.base

import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.http.model.*
import com.wlq.willymodule.common.http.model.handlingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

open class BaseRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> ApiResponse<T>): ApiResponse<T> {
        return call.invoke()
    }

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> HttpResult<T>,
        specifiedHandleError: ((httpError: HttpError) -> Unit)? = null,     //处理一些自定义的错误
        specifiedMessage: String
    ): HttpResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            val httpError = handlingException(e)
            commonHandleErrorBusiness(httpError)
            specifiedHandleError?.let { it(httpError) }
            HttpResult.Error(
                ApiException(
                    httpError.code, specifiedMessage.ifBlank { httpError.errorMsg }
                )
            )
        }
    }

    protected fun commonHandleErrorBusiness(httpError: HttpError) {
        //TODO 处理一些通用错误：根据错误类型实现某些逻辑
        LogUtils.i("commonHandleErrorBusiness", "${Thread.currentThread().name}:正在处理一些通用错误逻辑...")
    }

    suspend fun <T : Any> executeResponse(
        response: ApiResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): HttpResult<T> {
        return coroutineScope {
            if (response.errorCode != 0) {
                errorBlock?.let { it() }
                HttpResult.Error(ApiException(response.errorCode, response.errorMsg))
            } else {
                successBlock?.let { it() }
                HttpResult.Success(response.data)
            }
        }
    }
}