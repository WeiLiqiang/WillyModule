package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(
    value = [
        CameraSizeFor.SIZE_FOR_PREVIEW,
        CameraSizeFor.SIZE_FOR_PICTURE,
        CameraSizeFor.SIZE_FOR_VIDEO
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class CameraSizeFor {

    companion object {
        const val SIZE_FOR_PREVIEW = 0X0010
        const val SIZE_FOR_PICTURE = 0X0020
        const val SIZE_FOR_VIDEO = 0X0040
    }
}
