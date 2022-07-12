package com.wlq.willymodule.base.business.camera.enums

import androidx.annotation.IntDef

@IntDef(value = [CameraFace.FACE_REAR, CameraFace.FACE_FRONT])
@Retention(AnnotationRetention.SOURCE)
annotation class CameraFace {

    companion object {
        const val FACE_REAR = 0X0000
        const val FACE_FRONT = 0X0001
    }
}
