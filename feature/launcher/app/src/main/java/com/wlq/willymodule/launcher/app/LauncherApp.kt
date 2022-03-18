package com.wlq.willymodule.launcher.app

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.wlq.willymodule.base.base.BaseApplication
import com.wlq.willymodule.common.aop.methodtrace.TraceClass
import com.wlq.willymodule.common.aop.methodtrace.TraceMethod

@TraceClass
class LauncherApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initUtils()
    }

    @TraceMethod
    private fun initUtils() {
        Utils.init(this)
        LogUtils.getConfig().setConsoleSwitch(true).isLogSwitch = true
    }
}