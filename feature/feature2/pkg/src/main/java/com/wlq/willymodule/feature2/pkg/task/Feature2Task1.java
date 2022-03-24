package com.wlq.willymodule.feature2.pkg.task;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.common.aop.startup.StartupTask;
import com.wlq.willymodule.common.aop.startup.StartupTaskRegister;
import com.wlq.willymodule.feature0.export.contants.Feature0ExportConstants;
import com.wlq.willymodule.feature2.export.contants.Feature2ExportConstants;

@StartupTaskRegister(
        id = Feature2Task1.ID,
        idDependencies = {
                Feature0ExportConstants.FEATURE0_TASK_ID
        }
)
public class Feature2Task1 extends StartupTask {

    public static final String ID = Feature2ExportConstants.FEATURE2_TASK_ID;

    @Override
    public void startup() {
        LogUtils.i("startUp:Feature2Task1");
    }
}
