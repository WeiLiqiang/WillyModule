package com.wlq.willymodule.base.camera.config.calculator.impl

import android.util.SparseArray
import com.wlq.willymodule.base.camera.config.calculator.CameraSizeCalculator
import com.wlq.willymodule.base.camera.config.size.AspectRatio
import com.wlq.willymodule.base.camera.config.size.Size
import com.wlq.willymodule.base.camera.enums.MediaQuality
import com.wlq.willymodule.base.camera.util.CameraHelper
import com.wlq.willymodule.base.camera.util.XLog

class CameraSizeCalculatorImpl : CameraSizeCalculator {

    private var previewSizes: List<Size> = emptyList()
    private var pictureSizes: List<Size> = emptyList()
    private var videoSizes: List<Size> = emptyList()
    private var expectAspectRatio: AspectRatio? = null
    private var expectSize: Size? = null

    @MediaQuality
    private var mediaQuality = 0
    private val outPictureSizes = SparseArray<Size>()
    private val outVideoSizes = SparseArray<Size>()
    private val outPicturePreviewSizes = SparseArray<Size>()
    private val outVideoPreviewSizes = SparseArray<Size>()

    override fun init(
        expectAspectRatio: AspectRatio,
        expectSize: Size?,
        mediaQuality: Int,
        previewSizes: List<Size>,
        pictureSizes: List<Size>,
        videoSizes: List<Size>
    ) {
        this.expectAspectRatio = expectAspectRatio
        this.expectSize = expectSize
        this.mediaQuality = mediaQuality
        this.previewSizes = previewSizes
        this.pictureSizes = pictureSizes
        this.videoSizes = videoSizes
    }

    override fun changeExpectAspectRatio(expectAspectRatio: AspectRatio) {
        this.expectAspectRatio = expectAspectRatio
        outPictureSizes.clear()
        outPicturePreviewSizes.clear()
        outVideoSizes.clear()
        outVideoPreviewSizes.clear()
    }

    override fun changeExpectSize(expectSize: Size) {
        this.expectSize = expectSize
        outPictureSizes.clear()
        outPicturePreviewSizes.clear()
        outVideoSizes.clear()
        outVideoPreviewSizes.clear()
    }

    override fun changeMediaQuality(mediaQuality: Int) {
        this.mediaQuality = mediaQuality
        outPictureSizes.clear()
        outPicturePreviewSizes.clear()
        outVideoSizes.clear()
        outVideoPreviewSizes.clear()
    }

    override fun getPictureSize(cameraType: Int): Size? {
        var size = outPictureSizes[cameraType]
        if (size != null) return size
        size = CameraHelper.getSizeWithClosestRatioSizeAndQuality(
            pictureSizes, expectAspectRatio, expectSize, mediaQuality)
        outPictureSizes.put(cameraType, size)
        XLog.d("CameraSizeCalculator", "getPictureSize : $size")
        return size
    }

    override fun getPicturePreviewSize(cameraType: Int): Size? {
        var size = outPicturePreviewSizes[cameraType]
        if (size != null) return size
        size = CameraHelper.getSizeWithClosestRatio(previewSizes, getPictureSize(cameraType))
        outPicturePreviewSizes.put(cameraType, size)
        XLog.d("CameraSizeCalculator", "getPicturePreviewSize : $size")
        return size
    }

    override fun getVideoSize(cameraType: Int): Size? {
        var size = outVideoSizes[cameraType]
        if (size != null) return size
        // fix 2020-05-01 the video size might be empty if the camera don't separate video size and preview size
        val sizes = if (videoSizes.isNotEmpty()) videoSizes else previewSizes
        size = CameraHelper.getSizeWithClosestRatioSizeAndQuality(sizes, expectAspectRatio, expectSize, mediaQuality)
        outVideoSizes.put(cameraType, size)
        XLog.d("CameraSizeCalculator", "getVideoSize : $size")
        // fix 2020-05-01, should return the camera video size
        return size
    }

    override fun getVideoPreviewSize(cameraType: Int): Size? {
        var size = outVideoPreviewSizes[cameraType]
        if (size != null) return size
        size = CameraHelper.getSizeWithClosestRatio(previewSizes, getVideoSize(cameraType))
        outVideoPreviewSizes.put(cameraType, size)
        XLog.d("CameraSizeCalculator", "getVideoPreviewSize : $size")
        return size
    }
}