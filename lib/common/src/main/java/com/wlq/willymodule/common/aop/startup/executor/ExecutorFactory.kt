package com.wlq.willymodule.common.aop.startup.executor

import java.util.concurrent.Executor

interface ExecutorFactory {

    fun executor(): Executor
}