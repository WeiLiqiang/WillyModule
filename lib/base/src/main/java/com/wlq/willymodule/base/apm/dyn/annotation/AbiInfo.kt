package com.wlq.willymodule.base.apm.dyn.annotation

import androidx.annotation.StringDef

@StringDef(
    value = [
        AbiInfo.ARM32,
        AbiInfo.ARM64,
        AbiInfo.X86,
        AbiInfo.X86_64
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class AbiInfo {

    companion object {
        const val ARM64 = "arm64-v8a"
        const val ARM32 = "armeabi-v7a"
        const val X86 = "x86"
        const val X86_64 = "x86_64"
    }
}
