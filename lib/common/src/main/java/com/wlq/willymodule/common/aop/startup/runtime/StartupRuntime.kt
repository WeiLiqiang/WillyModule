package com.wlq.willymodule.common.aop.startup.runtime

import com.wlq.willymodule.common.aop.startup.ship.StartupShipUnLoader
import com.wlq.willymodule.common.aop.startup.StartupCore

internal object StartupRuntime : StartupShipUnLoader {

    var core: StartupCore? = null

    override fun unload(): Any? {
        return core?.unload()
    }
}