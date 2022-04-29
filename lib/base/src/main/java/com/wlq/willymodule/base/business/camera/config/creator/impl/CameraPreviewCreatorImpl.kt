package com.wlq.willymodule.base.business.camera.config.creator.impl

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import com.wlq.willymodule.base.business.camera.config.creator.CameraPreviewCreator
import com.wlq.willymodule.base.business.camera.preview.CameraPreview
import com.wlq.willymodule.base.business.camera.preview.impl.SurfacePreview
import com.wlq.willymodule.base.business.camera.preview.impl.TexturePreview

class CameraPreviewCreatorImpl : CameraPreviewCreator {

    override fun create(context: Context, parent: ViewGroup): CameraPreview {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TexturePreview(context, parent)
        } else SurfacePreview(context, parent)
    }
}