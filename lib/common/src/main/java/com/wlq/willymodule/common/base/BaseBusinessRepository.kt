package com.wlq.willymodule.common.base

import com.wlq.willymodule.base.business.network.cookie.store.MemoryCookieStore
import com.wlq.willymodule.base.business.network.cookie.store.PersistentCookieStore
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.http.model.*
import com.wlq.willymodule.common.model.store.UserInfoStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

open class BaseBusinessRepository {

    suspend fun <T : Any> apiCall(
        call: suspend () -> HttpResult<T>,
        handleCustomError: ((httpError: HttpError) -> Unit)? = null,     //处理一些自定义的错误
        specifiedMessage: String? = null     //指定错误提示信息
    ): HttpResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            val handleExceptionResult = handlingException(e)
            commonHandleErrorBusiness(handleExceptionResult)
            handleCustomError?.let { it(handleExceptionResult) }
            HttpResult.Error(
                ApiException(
                    handleExceptionResult.code,
                    if (specifiedMessage.isNullOrEmpty()) handleExceptionResult.errorMsg else specifiedMessage,
                    if (e.localizedMessage != null) e.localizedMessage else ""
                )
            )
        }
    }

    private fun commonHandleErrorBusiness(httpError: HttpError) {
        LogUtils.e("${httpError.code}:${httpError.errorMsg}")
        if (httpError.code == -1001) {
            //登录失败，清除用户信息和cookie
            UserInfoStore.clearUserInfo()
            PersistentCookieStore().removeAllCookie()
            MemoryCookieStore().removeAllCookie()
        }
    }

    suspend fun executeResponseString(
        response: Any,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): HttpResult.Success<Any> {
        return coroutineScope {
            successBlock?.let { it() }
            HttpResult.Success(response)
        }
    }

    suspend fun <T : Any> executeResponseHttpResult(
        response: ApiResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): HttpResult<T> {
        return coroutineScope {
            if (response.errorCode != 0) {
                errorBlock?.let { it() }
                HttpResult.Error(ApiException(response.errorCode, response.errorMsg, ""))
            } else {
                successBlock?.let { it() }
                HttpResult.Success(response.data)
            }
        }
    }
}