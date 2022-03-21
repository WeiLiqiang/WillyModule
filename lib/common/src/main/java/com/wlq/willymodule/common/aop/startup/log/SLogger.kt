package com.wlq.willymodule.common.aop.startup.log

interface SLogger {

    companion object {

        class CommonSLogger : SLogger {

            override fun i(msg: String) {
                println(msg)
            }
        }
    }

    fun i(msg: String)
}