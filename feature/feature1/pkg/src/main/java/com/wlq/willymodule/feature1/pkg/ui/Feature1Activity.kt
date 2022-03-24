package com.wlq.willymodule.feature1.pkg.ui

import android.content.Context
import com.wlq.willymodule.base.base.BaseActivity
import com.wlq.willymodule.feature1.pkg.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.wlq.willymodule.base.util.BusUtils

class Feature1Activity : BaseActivity() {
    @BusUtils.Bus(tag = TAG_ONE_PARAM, sticky = true)
    fun oneParamFun(param: String?) {
        LogUtils.i(param)
    }

    override fun bindLayout(): Int {
        return R.layout.activity_feature1
    }

    override fun initView(savedInstanceState: Bundle?, contentView: View?) {}

    override fun onStart() {
        super.onStart()
        BusUtils.register(this)
    }

    override fun doBusiness() {}
    override fun onStop() {
        super.onStop()
        BusUtils.unregister(this)
    }

    companion object {
        private const val TAG_ONE_PARAM = "TAG_ONE_PARAM"
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, Feature1Activity::class.java)
            context.startActivity(starter)
        }
    }
}