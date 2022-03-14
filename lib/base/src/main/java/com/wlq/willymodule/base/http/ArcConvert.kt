package com.wlq.willymodule.base.http

import java.lang.reflect.Type

interface ArcConvert {

    fun <T> convert(rawData: String, dataType: Type): ArcResponse<T>
}