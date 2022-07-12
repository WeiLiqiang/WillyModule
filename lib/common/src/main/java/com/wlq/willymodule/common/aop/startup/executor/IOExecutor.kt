package com.wlq.willymodule.common.aop.startup.executor

import com.wlq.willymodule.common.aop.startup.thread.StartupThreadFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class IOExecutor {

    companion object {
        @JvmStatic
        val INSTANCE: Executor by lazy {
            Executors.newCachedThreadPool(StartupThreadFactory("io"))
        }
    }

    class Factory : ExecutorFactory {
        override fun executor(): Executor = INSTANCE
    }
}