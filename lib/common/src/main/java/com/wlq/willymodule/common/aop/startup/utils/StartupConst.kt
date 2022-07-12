package com.wlq.willymodule.common.aop.startup.utils

class StartupConst {

    companion object {
        private const val STARTUP_PACKAGE = "com.wlq.willymodule.common.aop.startup"
        private const val GEN_PKG = "$STARTUP_PACKAGE.generated"
        const val STARTUP_LOADER_INIT_NAME = "$GEN_PKG.StartupLoaderInit"
        const val METHOD_INIT = "init"
    }
}