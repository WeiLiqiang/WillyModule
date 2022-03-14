package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(FlashMode.FLASH_ON, FlashMode.FLASH_OFF, FlashMode.FLASH_AUTO)
@Retention(AnnotationRetention.SOURCE)
annotation class FlashMode {
    companion object {
        const val FLASH_ON = 0
        const val FLASH_OFF = 1
        const val FLASH_AUTO = 2
    }
}
