package com.wlq.willymodule.base.business.camera.preview

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import com.wlq.willymodule.base.business.camera.config.size.Size
import com.wlq.willymodule.base.business.camera.enums.PreviewViewType

interface CameraPreview {

    fun setCameraPreviewCallback(callback: CameraPreviewCallback)

    val surface: Surface?

    @get:PreviewViewType
    val previewType: Int

    val surfaceHolder: SurfaceHolder?

    val surfaceTexture: SurfaceTexture?

    val isAvailable: Boolean

    val size: Size?

    val view: View?
}