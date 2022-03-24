package com.wlq.willymodule.launcher.app

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.wlq.willymodule.base.base.BaseApplication
import com.wlq.willymodule.common.aop.methodtrace.TraceClass
import com.wlq.willymodule.common.aop.methodtrace.TraceMethod
import com.wlq.willymodule.common.aop.startup.StartupCore
import com.wlq.willymodule.common.aop.startup.log.SLogger

class LauncherApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initStartUp()
    }

    private fun initStartUp() {
        StartupCore(this)
            .configAwaitTimeout(10000L)
            .configDebug(BuildConfig.DEBUG)
            .configLogger(object : SLogger {
                override fun i(msg: String) {
                    Log.i("StartupCore", msg)
                }
            })
            .startup()
    }
}