package com.wlq.willymodule.base.business.camera.config.creator.impl

import android.content.Context
import android.os.Build
import com.wlq.willymodule.base.business.camera.config.creator.CameraManagerCreator
import com.wlq.willymodule.base.business.camera.manager.CameraManager
import com.wlq.willymodule.base.business.camera.manager.impl.Camera1Manager
import com.wlq.willymodule.base.business.camera.manager.impl.Camera2Manager
import com.wlq.willymodule.base.business.camera.preview.CameraPreview
import com.wlq.willymodule.base.business.camera.util.CameraHelper

class CameraManagerCreatorImpl : CameraManagerCreator {

    override fun create(context: Context, cameraPreview: CameraPreview): CameraManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && CameraHelper.hasCamera2(context)) {
            Camera2Manager(cameraPreview)
        } else Camera1Manager(cameraPreview)
    }
}