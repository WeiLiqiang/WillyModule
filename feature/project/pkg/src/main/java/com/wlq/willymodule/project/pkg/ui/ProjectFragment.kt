package com.wlq.willymodule.project.pkg.ui

import android.os.Build
import android.os.Environment
import android.view.View
import com.arcsoft.face.FaceEngine
import com.arcsoft.face.VersionInfo
import com.wlq.willymodule.base.apm.dyn.ExternalSoLoadManager
import com.wlq.willymodule.base.apm.dyn.bean.DynamicSoInfo
import com.wlq.willymodule.base.apm.dyn.annotation.AbiInfo
import com.wlq.willymodule.base.apm.dyn.callback.DynamicSoCallback
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessFragment
import com.wlq.willymodule.project.pkg.databinding.FragmentProjectBinding
import kotlinx.android.synthetic.main.fragment_project.*
import java.io.File

class ProjectFragment :
    BaseBusinessFragment<FragmentProjectBinding, ProjectViewModel>(FragmentProjectBinding::inflate) {

    override val viewModel by lazy { ProjectViewModel() }
    private val externalSoLoadManager by lazy { ExternalSoLoadManager() }

    companion object {
        const val TAG_FRAGMENT = "ProjectFragment"
        fun getInstance(): ProjectFragment = ProjectFragment()
    }

    override fun onVisible(isFirstVisible: Boolean) {
        LogUtils.i(TAG_FRAGMENT, "onVisible isFirstVisible:$isFirstVisible")
    }

    override fun onInvisible() {
        LogUtils.i(TAG_FRAGMENT, "onInvisible")
    }

    override fun initView(view: View) {
        btn_load_local_so.setOnClickListener {
            val abiList = Build.SUPPORTED_ABIS
            var supportArm64 = false
            for (abi in abiList) {
                if (abi.equals(AbiInfo.ARM64)) {
                    supportArm64 = true
                    break
                }
            }
            if (supportArm64) load64FaceEngineSo1() else load32FaceEngineSo1()
        }
    }

    private fun load32FaceEngineSo1() {
        val info = DynamicSoInfo()
        info.url =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "libs" +
                    File.separator + "armeabi-v7a" + File.separator + "libarcsoft_face.so"
        info.name = "arcsoft"
        info.libName = "libarcsoft_face.so"
        info.abi = AbiInfo.ARM32
        info.hash = "d17491810ce46483d86b295851bf9ba7c562acbdd1745783f846d1cc07b000f9"
        info.size = 86465152
        externalSoLoadManager.checkAndLoadSo(info, object : DynamicSoCallback {

            override fun onSuccess(info: DynamicSoInfo) {
                LogUtils.i(TAG_FRAGMENT, "onSuccess:${info}")
                if (externalSoLoadManager.loadExternalSo(info)) {
                    load32FaceEngineSo2()
                }
            }

            override fun onProgress(progress: Int) {
            }

            override fun onFail() {
                LogUtils.i(TAG_FRAGMENT, "onFail")
            }
        })
    }

    private fun load32FaceEngineSo2() {
        val info = DynamicSoInfo()
        info.url =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "libs" +
                    File.separator + "armeabi-v7a" + File.separator + "libarcsoft_face_engine.so"
        info.name = "arcsoft"
        info.libName = "libarcsoft_face_engine.so"
        info.abi = AbiInfo.ARM32
        info.hash = "55d2d656496370ee8a6e9a72ca1e8d47fb2de7a6cbcf3b3c9444d16b2709e652"
        info.size = 768152
        externalSoLoadManager.checkAndLoadSo(info, object : DynamicSoCallback {

            override fun onSuccess(info: DynamicSoInfo) {
                LogUtils.i(TAG_FRAGMENT, "onSuccess:${info}")
                if (externalSoLoadManager.loadExternalSo(info)) {
                    val versionInfo = VersionInfo()
                    FaceEngine.getVersion(versionInfo)
                    LogUtils.i(TAG_FRAGMENT, "faceEngine version:$versionInfo")
                }
            }

            override fun onProgress(progress: Int) {
            }

            override fun onFail() {
                LogUtils.i(TAG_FRAGMENT, "onFail")
            }
        })
    }

    private fun load64FaceEngineSo1() {
        val info = DynamicSoInfo()
        info.url =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "libs" + File.separator + "arm64-v8a" + File.separator + "libarcsoft_face.so"
        info.name = "arcsoft"
        info.libName = "libarcsoft_face.so"
        info.abi = AbiInfo.ARM64
        info.hash = "9CF4DE72F6C41F03BAD6419020BD397D623B4C5C50AAEB33FC85528746B53E44"
        info.size = 86704848
        externalSoLoadManager.checkAndLoadSo(info, object : DynamicSoCallback {

            override fun onSuccess(info: DynamicSoInfo) {
                LogUtils.i(TAG_FRAGMENT, "onSuccess:${info}")
                if (externalSoLoadManager.loadExternalSo(info)) {
                    load64FaceEngineSo2()
                }
            }

            override fun onProgress(progress: Int) {
            }

            override fun onFail() {
                LogUtils.i(TAG_FRAGMENT, "onFail")
            }
        })
    }

    private fun load64FaceEngineSo2() {
        val info = DynamicSoInfo()
        info.url =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "libs" + File.separator + "arm64-v8a" + File.separator + "libarcsoft_face_engine.so"
        info.name = "arcsoft"
        info.libName = "libarcsoft_face_engine.so"
        info.abi = AbiInfo.ARM64
        info.hash = "08D315CC2549D0B8BD392722551A6AB5DB39C154F497793CA57E65F75A283B3F"
        info.size = 1329800
        externalSoLoadManager.checkAndLoadSo(info, object : DynamicSoCallback {

            override fun onSuccess(info: DynamicSoInfo) {
                LogUtils.i(TAG_FRAGMENT, "onSuccess:${info}")
                if (externalSoLoadManager.loadExternalSo(info)) {
                    val versionInfo = VersionInfo()
                    FaceEngine.getVersion(versionInfo)
                    LogUtils.i(TAG_FRAGMENT, "faceEngine version:$versionInfo")
                }
            }

            override fun onProgress(progress: Int) {
            }

            override fun onFail() {
                LogUtils.i(TAG_FRAGMENT, "onFail")
            }
        })
    }
}