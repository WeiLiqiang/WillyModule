package com.wlq.willymodule.common.task.utils

import com.wlq.willymodule.common.task.core.TaskInfo

class AnchorComparator : Comparator<TaskInfo> {

    override fun compare(o1: TaskInfo, o2: TaskInfo): Int {
        return o1.anchor.compareTo(o2.anchor)
    }
}