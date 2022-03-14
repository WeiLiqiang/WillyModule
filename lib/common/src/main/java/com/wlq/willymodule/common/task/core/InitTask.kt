package com.wlq.willymodule.common.task.core

import android.app.Application
import com.wlq.willymodule.common.task.core.Process.PROCESS_ALL

open interface InitTask {

    fun name(): String

    fun background(): Boolean = true

    fun anchor(): Boolean = false

    fun process(): Array<String> = arrayOf(PROCESS_ALL)

    fun depends(): Array<String> = arrayOf()

    suspend fun execute(application: Application)
}