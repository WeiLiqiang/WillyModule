package com.wlq.willymodule.base.business.camera.listener

import java.io.File

interface CameraPhotoListener {

    fun onPictureTaken(data: ByteArray, picture: File)
    fun onCaptureFailed(throwable: Throwable)
}