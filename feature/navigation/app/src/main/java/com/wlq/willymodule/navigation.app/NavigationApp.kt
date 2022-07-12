package com.wlq.willymodule.navigation.app

import android.util.Log
import com.wlq.willymodule.base.base.BaseApplication
import com.wlq.willymodule.common.aop.startup.StartupCore
import com.wlq.willymodule.common.aop.startup.log.SLogger

class NavigationApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initStartUp()
    }

    private fun initStartUp() {

    }
}