package com.wlq.willymodule.common.task.utils

import android.os.Looper

object ThreadUtils {

    fun isInMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }
}