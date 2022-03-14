package com.wlq.willymodule.base.camera.listener

import com.wlq.willymodule.base.camera.enums.CameraFace

interface CameraCloseListener {

    fun onCameraClosed(@CameraFace cameraFace: Int)
}