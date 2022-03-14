package com.wlq.willymodule.common.task

import com.wlq.willymodule.common.task.core.InitTask

class FinalTaskRegister {

    val taskList: MutableList<InitTask> = mutableListOf()

    init {
        init()
    }

    private fun init() {
    }

    fun register(initTask: InitTask) {
        taskList.add(initTask)
    }
}