package com.wlq.willymodule.base.business.camera.listener

import com.wlq.willymodule.base.business.camera.config.size.Size

interface CameraSizeListener {

    fun onPreviewSizeUpdated(previewSize: Size)
    fun onVideoSizeUpdated(videoSize: Size)
    fun onPictureSizeUpdated(pictureSize: Size)
}