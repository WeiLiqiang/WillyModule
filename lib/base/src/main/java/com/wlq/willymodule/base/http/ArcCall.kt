package com.wlq.willymodule.base.http

import java.io.IOException

interface ArcCall<T> {

    @Throws(IOException::class)
    fun execute(): ArcResponse<T>

    fun enqueue(callback: ArcCallback<T>)

    interface Factory {

        fun newCall(request: ArcRequest): ArcCall<*>
    }
}