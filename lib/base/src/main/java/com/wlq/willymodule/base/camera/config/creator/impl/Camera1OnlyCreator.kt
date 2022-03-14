package com.wlq.willymodule.base.camera.config.creator.impl

import android.content.Context
import com.wlq.willymodule.base.camera.config.creator.CameraManagerCreator
import com.wlq.willymodule.base.camera.manager.CameraManager
import com.wlq.willymodule.base.camera.manager.impl.Camera1Manager
import com.wlq.willymodule.base.camera.preview.CameraPreview

class Camera1OnlyCreator : CameraManagerCreator {

    override fun create(context: Context, cameraPreview: CameraPreview): CameraManager {
        return Camera1Manager(cameraPreview)
    }
}