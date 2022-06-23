package com.wlq.willymodule.common.base

import com.wlq.willymodule.base.business.network.cookie.CookieJarImpl
import com.wlq.willymodule.base.business.network.cookie.store.CookieStore
import com.wlq.willymodule.base.business.network.cookie.store.MemoryCookieStore
import com.wlq.willymodule.base.business.network.cookie.store.PersistentCookieStore
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.http.model.*
import com.wlq.willymodule.common.http.model.handlingException
import com.wlq.willymodule.common.http.retrofit.CommonRetrofitClient
import com.wlq.willymodule.common.model.store.UserInfoStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

open class BaseBusinessRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> ApiResponse<T>): ApiResponse<T> {
        return call.invoke()
    }

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> HttpResult<T>,
        handleCustomError: ((httpError: HttpError) -> Unit)? = null,     //处理一些自定义的错误
        specifiedMessage: String? = null     //指定错误提示信息
    ): HttpResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            val httpError = handlingException(e)
            commonHandleErrorBusiness(httpError)
            handleCustomError?.let { it(httpError) }
            HttpResult.Error(
                ApiException(
                    httpError.code,
                    if (specifiedMessage.isNullOrEmpty()) "${httpError.code}：${httpError.errorMsg}" else specifiedMessage
                )
            )
        }
    }

    private fun commonHandleErrorBusiness(httpError: HttpError) {
        if (httpError.code == -1001) {
            //登录失败，清楚用户信息和cookie
            UserInfoStore.clearUserInfo()
            PersistentCookieStore().removeAllCookie()
            MemoryCookieStore().removeAllCookie()
        }
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