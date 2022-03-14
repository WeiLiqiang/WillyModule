package com.wlq.willymodule.base.camera.listener

import java.io.File

interface CameraVideoListener {

    fun onVideoRecordStart()
    fun onVideoRecordStop(file: File)
    fun onVideoRecordError(throwable: Throwable)
}