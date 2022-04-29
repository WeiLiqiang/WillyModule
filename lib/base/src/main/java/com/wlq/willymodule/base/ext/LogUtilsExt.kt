package com.wlq.willymodule.base.ext

import com.wlq.willymodule.base.util.LogUtils

fun I(vararg contents: Any) {
    LogUtils.i(contents)
}

fun E(vararg contents: Any) {
    LogUtils.e(contents)
}

fun D(vararg contents: Any) {
    LogUtils.d(contents)
}