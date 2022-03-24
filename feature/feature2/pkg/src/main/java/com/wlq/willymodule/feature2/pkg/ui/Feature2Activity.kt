package com.wlq.willymodule.feature2.pkg.ui

import android.content.Context
import android.content.Intent
import com.wlq.willymodule.base.base.BaseVBActivity
import com.wlq.willymodule.feature2.pkg.databinding.ActivityFeature2Binding

class Feature2Activity : BaseVBActivity<ActivityFeature2Binding>(ActivityFeature2Binding::inflate) {

    override fun doBusiness() {

    }

    companion object {
        private const val TAG_ONE_PARAM = "TAG_ONE_PARAM"
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, Feature2Activity::class.java)
            context.startActivity(starter)
        }
    }
}