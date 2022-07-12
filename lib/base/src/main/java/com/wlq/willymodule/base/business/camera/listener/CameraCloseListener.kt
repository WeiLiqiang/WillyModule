package com.wlq.willymodule.base.business.camera.listener

import com.wlq.willymodule.base.business.camera.enums.CameraFace

interface CameraCloseListener {

    fun onCameraClosed(@CameraFace cameraFace: Int)
}