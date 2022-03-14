package com.wlq.willymodule.common.task.utils

import android.app.Application

class TaskStart constructor(
    val app: Application,
    val processName: String = ProcessUtils.getProcessName(app)
) {
    internal var isDebug : Boolean = false

    fun isDebug(isDebug: Boolean): TaskStart {
        this.isDebug = isDebug
        return this
    }

    fun start() {
        TaskManager.start(this)
    }
}