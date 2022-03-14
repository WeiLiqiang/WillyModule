package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(value = [MediaType.TYPE_PICTURE, MediaType.TYPE_VIDEO])
@Retention(AnnotationRetention.SOURCE)
annotation class MediaType {

    companion object {
        const val TYPE_PICTURE = 0
        const val TYPE_VIDEO = 1
    }
}
