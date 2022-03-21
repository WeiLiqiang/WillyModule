package com.wlq.willymodule.common.aop.startup.executor

import com.wlq.willymodule.common.aop.startup.thread.StartupThreadFactory
import kotlin.math.max
import kotlin.math.min
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class CPUExecutor {

    companion object {
        @JvmStatic
        val INSTANCE: Executor by lazy {
            val cpuCount = Runtime.getRuntime().availableProcessors()
            val corePoolCount = max(2, min(cpuCount - 1, 5))
            ThreadPoolExecutor(
                corePoolCount,
                corePoolCount,
                5,
                TimeUnit.SECONDS,
                LinkedBlockingDeque(),
                StartupThreadFactory("cpu"),
                null
            ).also {
                it.allowCoreThreadTimeOut(true)
            }
        }
    }

    class Factory : ExecutorFactory {
        override fun executor(): Executor = INSTANCE
    }
}