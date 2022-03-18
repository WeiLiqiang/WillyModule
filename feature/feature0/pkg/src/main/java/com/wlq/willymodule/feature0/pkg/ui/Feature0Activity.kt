package com.wlq.willymodule.feature0.pkg.ui

import android.content.Context
import com.wlq.willymodule.base.camera.CameraView
import com.wlq.willymodule.feature0.pkg.R
import com.wlq.willymodule.base.camera.listener.CameraOpenListener
import com.wlq.willymodule.base.camera.listener.CameraPreviewListener
import com.wlq.willymodule.base.camera.listener.CameraCloseListener
import com.wlq.willymodule.feature1.export.api.Feature1Api
import com.wlq.willymodule.feature1.export.model.Feature1Param
import com.blankj.utilcode.util.ToastUtils
import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import com.wlq.willymodule.base.base.BaseVBActivity
import com.wlq.willymodule.base.camera.config.size.Size
import com.wlq.willymodule.base.http.ArcResponse
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.base.util.BusUtils
import com.wlq.willymodule.common.aop.methodtrace.TimeTrace
import com.wlq.willymodule.common.aop.methodtrace.TraceClass
import com.wlq.willymodule.common.aop.methodtrace.TraceMethod
import com.wlq.willymodule.common.aop.methodtrace.custom.CustomSystemTrace
import com.wlq.willymodule.feature0.pkg.databinding.ActivityFeature0Binding
import com.wlq.willymodule.feature0.pkg.http.ApiFactory
import com.wlq.willymodule.feature0.pkg.http.ArcApiService

@TraceClass(traceAllMethod = true)
class Feature0Activity : BaseVBActivity<ActivityFeature0Binding>(ActivityFeature0Binding::inflate) {

    @TraceMethod()
    override fun onResume() {
        super.onResume()
        binding.cameraView.openCamera(object : CameraOpenListener {

            override fun onCameraOpened(cameraFace: Int) {
                LogUtils.i("onCameraOpened:$cameraFace")
            }

            override fun onCameraOpenError(throwable: Throwable) {
                LogUtils.e("onCameraOpened:" + throwable.message)
            }
        })
        binding.cameraView.setCameraPreviewListener(object : CameraPreviewListener {
            override fun onPreviewFrame(data: ByteArray, size: Size, format: Int) {

            }
        })
    }

    override fun onPause() {
        super.onPause()
        binding.cameraView.closeCamera(object : CameraCloseListener {
            override fun onCameraClosed(cameraFace: Int) {}
        })
    }

    @TraceMethod()
    override fun doBusiness() {
        binding.btnGoToFeature1.setOnClickListener {
            BusUtils.postSticky("TAG_ONE_PARAM", "我来自Feature0Activity的粘性事件")
            val feature1Result = ApiUtils.getApi(
                Feature1Api::class.java
            )?.startFeature1Activity(this@Feature0Activity, Feature1Param("From Feature0"))
            if (feature1Result != null) {
                ToastUtils.showShort(feature1Result.name)
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, Feature0Activity::class.java)
            context.startActivity(starter)
        }
    }
}