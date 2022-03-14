package com.wlq.willymodule.base.camera.listener

import com.wlq.willymodule.base.camera.config.size.Size

interface CameraSizeListener {

    fun onPreviewSizeUpdated(previewSize: Size)
    fun onVideoSizeUpdated(videoSize: Size)
    fun onPictureSizeUpdated(pictureSize: Size)
}