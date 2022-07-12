package com.wlq.willymodule.common.aop.startup

import com.wlq.willymodule.common.aop.startup.ship.StartupShipUnLoader
import com.wlq.willymodule.common.aop.startup.executor.BlockExecutor
import com.wlq.willymodule.common.aop.startup.executor.ExecutorFactory
import com.wlq.willymodule.common.aop.startup.runtime.StartupRuntime
import kotlin.reflect.KClass

abstract class StartupTask : StartupShipUnLoader, Runnable  {

    /**
     * 执行任务
     * @return T 可有可无
     */
    abstract fun startup()

    override fun unload(): Any? {
        return StartupRuntime.unload()
    }

    override fun run() {
        startup()
    }

    val id: String
        get() = register.id
    val idDependencies: Array<String>
        get() = register.idDependencies
    val executorFactory: KClass<out ExecutorFactory>
        get() = register.executorFactory
    val blockWhenAsync: Boolean
        get() = register.blockWhenAsync
    val process: String
        get() = register.process
    val priority: Int
        get() = register.priority
    val isSync: Boolean
        get() = register.executorFactory == BlockExecutor.Factory::class
    val isBlock: Boolean
        get() = register.blockWhenAsync && !isSync

    private val register: StartupTaskRegister by lazy {
        javaClass.getAnnotation(StartupTaskRegister::class.java)
    }
    internal var awaitTime: Long? = null
    internal var startupTime: Long? = null
}