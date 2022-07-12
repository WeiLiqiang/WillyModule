package com.wlq.willymodule.common.aop.startup.sort

import com.wlq.willymodule.common.aop.startup.StartupTask

internal data class StartupSortResult(
    val startupList: List<StartupTask>,                  // 任务集合
    val startupMap: Map<String, StartupTask>,            // 任务key: 任务
    val startupChildrenMap: Map<String, List<String>>    // 任务key: 子任务key集合)
)