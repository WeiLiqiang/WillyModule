package com.wlq.willymodule.common.task

import android.content.Context
import android.content.res.Configuration
import com.wlq.willymodule.common.task.application.IApplication

internal class FinalAppRegister {

    private val appList: MutableList<IApplication> = mutableListOf()

    init {
        init()
    }

    private fun init() {

    }

    fun register(app: IApplication) {
        appList.add(app)
    }

    fun attachBaseContext(context: Context) {
        appList.forEach {
            it.attachBaseContext(context)
        }
    }

    fun onCreate() {
        appList.forEach {
            it.onCreate()
        }
    }

    fun onTerminate() {
        appList.forEach {
            it.onTerminate()
        }
    }

    fun onLowMemory() {
        appList.forEach {
            it.onLowMemory()
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        appList.forEach {
            it.onConfigurationChanged(newConfig)
        }
    }
}