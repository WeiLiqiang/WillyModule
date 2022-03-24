package com.wlq.willymodule.common.aop.startup.log

import com.wlq.willymodule.base.util.LogUtils

interface SLogger {

    companion object {

        class CommonSLogger : SLogger {

            override fun i(msg: String) {
                LogUtils.i(msg)
            }
        }
    }

    fun i(msg: String)
}