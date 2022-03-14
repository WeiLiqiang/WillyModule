package com.wlq.willymodule.common.http

import com.wlq.willymodule.base.http.ArcInterceptor

class HeaderInterceptor : ArcInterceptor {

    override fun intercept(chain: ArcInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            request.addHeader("auth-token", "MPIADF12312ADFA==")
        } else if (chain.response() != null) {

        }
        return false
    }
}