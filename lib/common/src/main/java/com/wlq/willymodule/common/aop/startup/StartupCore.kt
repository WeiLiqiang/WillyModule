package com.wlq.willymodule.common.aop.startup

import com.wlq.willymodule.common.aop.startup.ship.StartupShipUnLoader
import com.wlq.willymodule.common.aop.startup.dispatcher.StartupDispatcher
import com.wlq.willymodule.common.aop.startup.log.SLogger
import com.wlq.willymodule.common.aop.startup.runtime.StartupRuntime
import com.wlq.willymodule.common.aop.startup.sort.StartupSort
import com.wlq.willymodule.common.aop.startup.trace.STracer
import com.wlq.willymodule.common.aop.startup.utils.*
import java.lang.reflect.InvocationTargetException
import java.util.*

open class StartupCore(private val shipStuff: Any?) : StartupShipUnLoader {

    open var awaitTimeout: Long = 5000L
    open var debug: Boolean = false
    open var logger: SLogger = SLogger.Companion.CommonSLogger()
    open var tracer: STracer = STracer.Companion.CommonSTracer()

    open val allStartup = mutableListOf<StartupTask>()

    open fun startup() {
        StartupRuntime.core = this

        trace("startup") {
            loadRegister()
            val sortResult = StartupSort.sort(Collections.unmodifiableList(allStartup))
            StartupDispatcher(sortResult)
                .dispatch(awaitTimeout)
        }
    }

    open fun loadRegister() {
        trace("loadRegister") {
            runCatching {
                Class.forName(StartupConst.STARTUP_LOADER_INIT_NAME)
                    .getMethod(StartupConst.METHOD_INIT, StartupCore::class.java)
                    .invoke(null, this)
            }.takeIf {
                it.isFailure
            }
        }
    }

    override fun unload(): Any? {
        return shipStuff
    }

    /**
     * see com.zyhang.startup.generated.StartupLoaderInit
     * see com.zyhang.startup.plugin.StartupPlugin
     */
    open fun register(startup: StartupTask) {
        allStartup.add(startup)
    }

    open fun configAwaitTimeout(timeout: Long) = apply {
        this.awaitTimeout = timeout
    }

    open fun configDebug(debug: Boolean) = apply {
        this.debug = debug
    }

    open fun configLogger(logger: SLogger) = apply {
        this.logger = logger
    }

    open fun configTracer(tracer: STracer) = apply {
        this.tracer = tracer
    }
}