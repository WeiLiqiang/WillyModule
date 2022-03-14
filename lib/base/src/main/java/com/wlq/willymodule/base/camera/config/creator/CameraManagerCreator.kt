package com.wlq.willymodule.base.camera.config.creator

import android.content.Context
import com.wlq.willymodule.base.camera.manager.CameraManager
import com.wlq.willymodule.base.camera.preview.CameraPreview

interface CameraManagerCreator {

    fun create(context: Context, cameraPreview: CameraPreview): CameraManager
}