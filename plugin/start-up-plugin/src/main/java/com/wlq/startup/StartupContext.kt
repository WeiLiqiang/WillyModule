package com.wlq.startup

import com.android.build.gradle.AppExtension
import com.ss.android.ugc.bytex.common.BaseContext
import org.gradle.api.Project

class StartupContext(project: Project?, android: AppExtension?, extension: StartupExtension?) :
    BaseContext<StartupExtension>(project, android, extension)