package com.wlq.willymodule.base.business.camera.enums

import androidx.annotation.IntDef

@IntDef(
    value = [
        CameraType.TYPE_CAMERA1,
        CameraType.TYPE_CAMERA2
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class CameraType {

    companion object {
        const val TYPE_CAMERA1 = 0X0100
        const val TYPE_CAMERA2 = 0X0200
    }
}
