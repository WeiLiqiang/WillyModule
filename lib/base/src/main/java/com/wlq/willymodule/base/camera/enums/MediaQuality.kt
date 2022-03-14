package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(value = [MediaQuality.QUALITY_AUTO,
    MediaQuality.QUALITY_LOWEST,
    MediaQuality.QUALITY_LOW,
    MediaQuality.QUALITY_MEDIUM,
    MediaQuality.QUALITY_HIGH,
    MediaQuality.QUALITY_HIGHEST])
@Retention(AnnotationRetention.SOURCE)
annotation class MediaQuality {
    companion object {
        const val QUALITY_AUTO = 0
        const val QUALITY_LOWEST = 1
        const val QUALITY_LOW = 2
        const val QUALITY_MEDIUM = 3
        const val QUALITY_HIGH = 4
        const val QUALITY_HIGHEST = 5
    }
}
