package com.wlq.willymodule.base.business.camera.preview.impl

import com.wlq.willymodule.base.business.camera.config.size.Size
import com.wlq.willymodule.base.business.camera.preview.CameraPreview
import com.wlq.willymodule.base.business.camera.preview.CameraPreviewCallback

abstract class BaseCameraPreview protected constructor() : CameraPreview {

    private var width = 0
    private var height = 0
    private var cameraPreviewCallback: CameraPreviewCallback? = null

    override fun setCameraPreviewCallback(callback: CameraPreviewCallback) {
        this.cameraPreviewCallback = callback
    }

    protected fun notifyPreviewAvailable() {
        cameraPreviewCallback?.onAvailable(this)
    }

    override val isAvailable: Boolean
        get() = width > 0 && height > 0

    override val size: Size
        get() = Size.of(width, height)

    protected fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}