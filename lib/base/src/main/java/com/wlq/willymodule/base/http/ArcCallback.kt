package com.wlq.willymodule.base.http

interface ArcCallback<T> {

    fun onSuccess(response: ArcResponse<T>)

    fun onFailed(throwable: Throwable)
}