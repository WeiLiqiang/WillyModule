package com.wlq.willymodule.feature2.pkg.task;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.common.aop.startup.StartupTask;
import com.wlq.willymodule.common.aop.startup.StartupTaskRegister;
import com.wlq.willymodule.feature1.export.contants.Feature1ExportConstants;
import com.wlq.willymodule.feature2.export.contants.Feature2ExportConstants;

@StartupTaskRegister(
        id = Feature2Task2.ID,
        idDependencies = {
                Feature1ExportConstants.FEATURE1_TASK_ID,
        }
)
public class Feature2Task2 extends StartupTask {

    public static final String ID = Feature2ExportConstants.FEATURE2_TASK2_ID;

    @Override
    public void startup() {
        LogUtils.i("startUp:Feature2Task2");
    }
}
