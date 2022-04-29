package com.wlq.willymodule.base.business.camera.manager

import android.content.Context
import com.wlq.willymodule.base.business.camera.config.size.AspectRatio
import com.wlq.willymodule.base.business.camera.config.size.Size
import com.wlq.willymodule.base.business.camera.listener.*
import com.wlq.willymodule.base.business.camera.config.size.SizeMap
import com.wlq.willymodule.base.business.camera.enums.*
import java.io.File

interface CameraManager {

    fun initialize(context: Context)

    fun openCamera(cameraOpenListener: CameraOpenListener?)

    val isCameraOpened: Boolean

    @get:CameraFace
    var cameraFace: Int

    fun switchCamera(@CameraFace cameraFace: Int)

    @get:MediaType
    var mediaType: Int

    var isVoiceEnable: Boolean

    var isAutoFocus: Boolean

    @get:FlashMode
    var flashMode: Int

    var zoom: Float

    fun getMaxZoom(): Float

    fun setExpectSize(expectSize: Size)

    fun setExpectAspectRatio(expectAspectRatio: AspectRatio)

    fun setMediaQuality(@MediaQuality mediaQuality: Int)

    fun getSize(@CameraSizeFor sizeFor: Int): Size?

    fun getSizes(@CameraSizeFor sizeFor: Int): SizeMap?

    fun getAspectRatio(@CameraSizeFor sizeFor: Int): AspectRatio?

    var displayOrientation: Int

    fun addCameraSizeListener(cameraSizeListener: CameraSizeListener)

    fun setCameraPreviewListener(cameraPreviewListener: CameraPreviewListener)

    fun takePicture(fileToSave: File, cameraPhotoListener: CameraPhotoListener?)

    var videoFileSize: Long

    var videoDuration: Int

    fun startVideoRecord(file: File, cameraVideoListener: CameraVideoListener?)

    fun stopVideoRecord()

    fun resumePreview()

    fun closeCamera(cameraCloseListener: CameraCloseListener?)

    fun releaseCamera()
}