package com.wlq.willymodule.base.business.camera.config.creator.impl

import android.content.Context
import android.view.ViewGroup
import com.wlq.willymodule.base.business.camera.config.creator.CameraPreviewCreator
import com.wlq.willymodule.base.business.camera.preview.CameraPreview
import com.wlq.willymodule.base.business.camera.preview.impl.SurfacePreview

class SurfaceViewOnlyCreator  : CameraPreviewCreator {
    override fun create(
        context: Context,
        parent: ViewGroup
    ): CameraPreview {
        return SurfacePreview(context, parent)
    }
}