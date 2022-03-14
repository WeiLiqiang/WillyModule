package com.wlq.willymodule.base.http

interface ArcInterceptor {

    fun intercept(chain: Chain): Boolean

    interface Chain {

        val isRequestPeriod: Boolean get() = false

        fun request(): ArcRequest

        fun response(): ArcResponse<*>?
    }
}