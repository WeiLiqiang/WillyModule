package com.wlq.willymodule.base.business.camera.listener

import com.wlq.willymodule.base.business.camera.config.size.Size

interface CameraPreviewListener {

    fun onPreviewFrame(data: ByteArray, size: Size, format: Int)
}