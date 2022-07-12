package com.wlq.willymodule.common.aop.startup.trace

import com.wlq.willymodule.common.aop.startup.utils.log

interface STracer {

    companion object {
        open class CommonSTracer : STracer {

            override fun beginSection(sectionName: String) {
            }

            override fun endSection(sectionName: String) {
            }
        }
    }

    fun beginSection(sectionName: String)
    fun endSection(sectionName: String)
}