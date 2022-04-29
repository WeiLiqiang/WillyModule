package com.wlq.willymodule.base.business.camera.config.creator

import android.content.Context
import android.view.ViewGroup
import com.wlq.willymodule.base.business.camera.preview.CameraPreview

interface CameraPreviewCreator {

    fun create(context: Context, parent: ViewGroup): CameraPreview
}