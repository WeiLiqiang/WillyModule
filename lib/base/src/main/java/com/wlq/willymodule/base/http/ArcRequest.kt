package com.wlq.willymodule.base.http

import androidx.annotation.IntDef
import java.lang.IllegalStateException
import java.lang.reflect.Type

open class ArcRequest {

    @METHOD
    var httpMethod: Int = 0
    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, String>? = null
    var domainUrl: String? = null
    var relativeUrl: String? = null
    var returnType: Type? = null
    var formPost: Boolean = true

    @IntDef(value = [METHOD.GET, METHOD.POST])
    annotation class METHOD {

        companion object {
            const val GET = 0
            const val POST = 1
        }
    }

    fun endPointUrl(): String {
        if (relativeUrl == null) {
            throw IllegalStateException("relative url must not be null")
        }
//        if (!relativeUrl!!.startsWith("/")) {
//            return domainUrl + relativeUrl
//        }
//        val index = domainUrl!!.lastIndexOf("/")
//        return domainUrl!!.substring(0, index) + relativeUrl
        return domainUrl + relativeUrl
    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![name] = value
    }
}