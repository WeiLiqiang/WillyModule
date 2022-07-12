package com.wlq.willymodule.base.business.camera.config.creator

import android.content.Context
import com.wlq.willymodule.base.business.camera.manager.CameraManager
import com.wlq.willymodule.base.business.camera.preview.CameraPreview

interface CameraManagerCreator {

    fun create(context: Context, cameraPreview: CameraPreview): CameraManager
}