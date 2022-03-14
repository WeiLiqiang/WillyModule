package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(value = [PreviewViewType.SURFACE_VIEW, PreviewViewType.TEXTURE_VIEW])
@Retention(AnnotationRetention.SOURCE)
annotation class PreviewViewType {

    companion object {
        const val SURFACE_VIEW = 0
        const val TEXTURE_VIEW = 1
    }
}