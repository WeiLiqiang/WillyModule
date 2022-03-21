package com.wlq.willymodule.launcher.mock.pkg.ui

import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.launcher.mock.pkg.ui.task.TaskLauncher
import com.wlq.willymodule.mock.export.api.LauncherApi

@ApiUtils.Api
class LauncherApiImpl : LauncherApi() {

    override fun getTaskLauncherId(): String {
        return TaskLauncher.id
    }
}