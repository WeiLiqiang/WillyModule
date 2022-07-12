package com.wlq.willymodule.base.business.camera.config.creator.impl

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.wlq.willymodule.base.business.camera.config.creator.CameraManagerCreator
import com.wlq.willymodule.base.business.camera.manager.CameraManager
import com.wlq.willymodule.base.business.camera.manager.impl.Camera2Manager
import com.wlq.willymodule.base.business.camera.preview.CameraPreview

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class Camera2OnlyCreator : CameraManagerCreator {

    override fun create(context: Context, cameraPreview: CameraPreview): CameraManager {
        return Camera2Manager(cameraPreview)
    }
}