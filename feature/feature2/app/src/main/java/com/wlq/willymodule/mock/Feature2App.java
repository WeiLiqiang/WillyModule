package com.wlq.willymodule.mock;

import com.wlq.willymodule.base.base.BaseApplication;
import com.wlq.willymodule.base.util.LogUtils;
import com.wlq.willymodule.common.aop.startup.StartupCore;

public class Feature2App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initStartUp();
    }

    private void initStartUp() {
        new StartupCore(this)
                .configAwaitTimeout(10000L)
                .configDebug(BuildConfig.DEBUG)
                .configLogger(msg -> LogUtils.i("Feature2App", msg))
            .startup();
    }
}
