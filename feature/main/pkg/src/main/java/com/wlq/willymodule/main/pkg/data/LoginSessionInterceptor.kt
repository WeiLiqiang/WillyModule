package com.wlq.willymodule.main.pkg.data

import com.wlq.willymodule.base.business.network.core.MoshiHelper
import com.wlq.willymodule.base.util.LogUtils
import okhttp3.Interceptor
import okhttp3.Response

class LoginSessionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val headers = response.headers
        LogUtils.i(MoshiHelper.toJson(headers))
        return response
    }
}