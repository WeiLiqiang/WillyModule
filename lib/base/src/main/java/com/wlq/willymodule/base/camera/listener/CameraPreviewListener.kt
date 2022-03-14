package com.wlq.willymodule.base.camera.listener

import com.wlq.willymodule.base.camera.config.size.Size

interface CameraPreviewListener {

    fun onPreviewFrame(data: ByteArray, size: Size, format: Int)
}