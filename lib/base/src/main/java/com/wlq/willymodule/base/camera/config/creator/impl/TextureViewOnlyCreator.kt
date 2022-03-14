package com.wlq.willymodule.base.camera.config.creator.impl

import android.content.Context
import android.view.ViewGroup
import com.wlq.willymodule.base.camera.config.creator.CameraPreviewCreator
import com.wlq.willymodule.base.camera.preview.CameraPreview
import com.wlq.willymodule.base.camera.preview.impl.TexturePreview

class TextureViewOnlyCreator : CameraPreviewCreator {
    override fun create(
        context: Context,
        parent: ViewGroup
    ): CameraPreview {
        return TexturePreview(context, parent)
    }
}