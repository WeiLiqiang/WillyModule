package com.wlq.willymodule.feature1.pkg.task;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.common.aop.startup.StartupTask;
import com.wlq.willymodule.common.aop.startup.StartupTaskRegister;
import com.wlq.willymodule.feature0.export.contants.Feature0ExportConstants;
import com.wlq.willymodule.feature1.export.contants.Feature1ExportConstants;

@StartupTaskRegister(
        id = Feature1Task1.ID,
        idDependencies = {Feature0ExportConstants.FEATURE0_TASK_ID}
)
public class Feature1Task1 extends StartupTask {

    public static final String ID = Feature1ExportConstants.FEATURE1_TASK_ID;

    @Override
    public void startup() {
        LogUtils.i("startUp:Feature1Task1");
    }
}
