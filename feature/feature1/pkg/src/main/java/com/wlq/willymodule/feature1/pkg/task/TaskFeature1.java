package com.wlq.willymodule.feature1.pkg.task;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.common.aop.startup.StartupTask;
import com.wlq.willymodule.common.aop.startup.StartupTaskRegister;
import com.wlq.willymodule.common.aop.startup.executor.IOExecutor;

@StartupTaskRegister(
        id = TaskFeature1.ID,
        idDependencies = {"TaskLauncher.id"},
        executorFactory = IOExecutor.Factory.class
)
public class TaskFeature1 extends StartupTask {

    public static final String ID = "TaskFeature1.id";

    @Override
    public void startup() {
        LogUtils.i("startUp:TaskFeature1");
    }
}
