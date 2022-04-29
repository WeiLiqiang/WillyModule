package com.wlq.willymodule.base.business.camera.config.calculator

import com.wlq.willymodule.base.business.camera.config.size.AspectRatio
import com.wlq.willymodule.base.business.camera.config.size.Size
import com.wlq.willymodule.base.business.camera.enums.CameraType
import com.wlq.willymodule.base.business.camera.enums.MediaQuality

interface CameraSizeCalculator {

    fun init(
        expectAspectRatio: AspectRatio,
        expectSize: Size?,
        @MediaQuality mediaQuality: Int,
        previewSizes: List<Size>,
        pictureSizes: List<Size>,
        videoSizes: List<Size>
    )

    fun changeExpectAspectRatio(expectAspectRatio: AspectRatio)

    fun changeExpectSize(expectSize: Size)

    fun changeMediaQuality(@MediaQuality mediaQuality: Int)

    fun getPictureSize(@CameraType cameraType: Int): Size?

    fun getPicturePreviewSize(@CameraType cameraType: Int): Size?

    fun getVideoSize(@CameraType cameraType: Int): Size?

    fun getVideoPreviewSize(@CameraType cameraType: Int): Size?
}