package com.wlq.willymodule.common.task.core

import android.app.Application
import com.wlq.willymodule.common.task.utils.ProcessUtils

data class TaskInfo(
    var name: String,
    var background: Boolean,
    var anchor: Boolean,
    var process: Set<String>,
    var depends: Set<String>,
    var task: InitTask,
    var children: MutableSet<TaskInfo> = mutableSetOf(),
    var priority: Int = 0
)

fun List<InitTask>.toTaskInfo(application: Application, processName: String): List<TaskInfo> {
    val result = mutableListOf<TaskInfo>()
    for (initTask in this) {
        if (ProcessUtils.isMatchProgress(application, processName, initTask)) {
            result.add(
                TaskInfo(
                    initTask.name(),
                    initTask.background(),
                    initTask.anchor(),
                    HashSet(listOf(*initTask.process())),
                    HashSet(listOf(*initTask.depends())),
                    initTask
                )
            )
        }
    }
    return result
}