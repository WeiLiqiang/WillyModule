package com.wlq.willymodule.launcher.mock.pkg.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.blankj.utilcode.util.ToastUtils
import com.wlq.willymodule.base.BaseActivity
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.feature0.export.Feature0Param
import com.wlq.willymodule.feature0.export.api.Feature0Api
import com.wlq.willymodule.feature1.export.api.Feature1Api
import com.wlq.willymodule.feature1.export.model.Feature1Param
import com.wlq.willymodule.launcher.mock.pkg.R

class LauncherActivity : BaseActivity() {
    override fun bindLayout(): Int {
        return R.layout.activity_launcher
    }

    override fun initView(savedInstanceState: Bundle?, contentView: View?) {

    }

    override fun doBusiness() {
        findViewById<Button>(R.id.btn_feature0).setOnClickListener {
            val result = ApiUtils.getApi(Feature0Api::class.java)
                ?.startFeature0Activity(this, Feature0Param("From Launcher Button0"))
            if (result != null) {
                ToastUtils.showShort(result.name)
            }
        }

        findViewById<Button>(R.id.btn_feature1).setOnClickListener {
            val result = ApiUtils.getApi(Feature1Api::class.java)
                ?.startFeature1Activity(this, Feature1Param("From Launcher Button1"))
            if (result != null) {
                ToastUtils.showShort(result.name)
            }
        }
    }
}