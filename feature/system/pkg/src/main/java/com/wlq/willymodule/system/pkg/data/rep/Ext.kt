package com.wlq.willymodule.system.pkg.data.rep

import com.wlq.willymodule.system.pkg.data.bean.SystemChild

fun transFormSystemChild(children: List<SystemChild>): String {
    return children.joinToString("     ", transform = { child -> child.name })
}