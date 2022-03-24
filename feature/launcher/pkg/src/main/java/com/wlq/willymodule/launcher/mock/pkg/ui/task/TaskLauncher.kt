package com.wlq.willymodule.launcher.mock.pkg.ui.task

import com.blankj.utilcode.util.LogUtils
import com.wlq.willymodule.common.aop.startup.StartupTask
import com.wlq.willymodule.common.aop.startup.StartupTaskRegister
import com.wlq.willymodule.mock.export.api.constants.LauncherExportConstants

@StartupTaskRegister(id = TaskLauncher.id)
class TaskLauncher : StartupTask() {

    override fun startup() {
        LogUtils.i("startUp:TaskLauncher")
    }

    companion object {
        const val id = LauncherExportConstants.LAUNCHER_TASK_ID
    }
}