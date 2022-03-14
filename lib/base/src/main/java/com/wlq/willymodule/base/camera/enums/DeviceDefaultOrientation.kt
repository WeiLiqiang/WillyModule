package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(
    DeviceDefaultOrientation.ORIENTATION_PORTRAIT,
    DeviceDefaultOrientation.ORIENTATION_LANDSCAPE
)
@Retention(AnnotationRetention.SOURCE)
annotation class DeviceDefaultOrientation {
    companion object {
        const val ORIENTATION_PORTRAIT = 0x01
        const val ORIENTATION_LANDSCAPE = 0x02
    }
}
