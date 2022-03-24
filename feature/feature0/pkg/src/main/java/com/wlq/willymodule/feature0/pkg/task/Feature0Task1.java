package com.wlq.willymodule.feature0.pkg.task;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.common.aop.startup.StartupTask;
import com.wlq.willymodule.common.aop.startup.StartupTaskRegister;
import com.wlq.willymodule.feature0.export.contants.Feature0ExportConstants;
import com.wlq.willymodule.mock.export.api.constants.LauncherExportConstants;

@StartupTaskRegister(
        id = Feature0Task1.ID,
        idDependencies = {LauncherExportConstants.LAUNCHER_TASK_ID}
)
public class Feature0Task1 extends StartupTask {

    public static final String ID = Feature0ExportConstants.FEATURE0_TASK_ID;

    @Override
    public void startup() {
        LogUtils.i("startUp:Feature0Task1");
    }
}
