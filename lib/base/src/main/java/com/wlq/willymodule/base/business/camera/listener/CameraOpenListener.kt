package com.wlq.willymodule.base.business.camera.listener

import com.wlq.willymodule.base.business.camera.enums.CameraFace

interface CameraOpenListener {

    fun onCameraOpened(@CameraFace cameraFace: Int)
    fun onCameraOpenError(throwable: Throwable)
}