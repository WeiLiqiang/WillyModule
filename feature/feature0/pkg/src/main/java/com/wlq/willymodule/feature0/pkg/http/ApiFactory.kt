package com.wlq.willymodule.feature0.pkg.http

import com.wlq.willymodule.base.http.ArcRestful
import com.wlq.willymodule.common.http.HeaderInterceptor
import com.wlq.willymodule.common.http.callfactory.RetrofitCallFactory

object ApiFactory {

    private const val baselUrl = "https://wanandroid.com/"
    var arcRestful = ArcRestful(baselUrl, RetrofitCallFactory(baselUrl))

    init {
        arcRestful.addInterceptor(HeaderInterceptor())
    }

    fun <T> create(service: Class<T>): T {
        return arcRestful.create(service)
    }
}