package com.wlq.willymodule.base.camera.listener

import com.wlq.willymodule.base.camera.enums.CameraFace

interface CameraOpenListener {

    fun onCameraOpened(@CameraFace cameraFace: Int)
    fun onCameraOpenError(throwable: Throwable)
}