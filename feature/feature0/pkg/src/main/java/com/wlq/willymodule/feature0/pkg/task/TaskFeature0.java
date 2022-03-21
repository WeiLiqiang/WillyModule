package com.wlq.willymodule.feature0.pkg.task;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.common.aop.startup.StartupTask;
import com.wlq.willymodule.common.aop.startup.StartupTaskRegister;
import com.wlq.willymodule.common.aop.startup.executor.IOExecutor;

@StartupTaskRegister(
        id = TaskFeature0.ID,
        idDependencies = {"TaskLauncher.id"},
        executorFactory = IOExecutor.Factory.class
)
public class TaskFeature0 extends StartupTask {

    public static final String ID = "TaskFeature0.id";

    @Override
    public void startup() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LogUtils.i("startUp:TaskFeature0");
        }
    }
}
