package com.wlq.willymodule.wx.pkg.ui

import android.view.View
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessFragment
import com.wlq.willymodule.wx.pkg.databinding.FragmentWxBinding

class WxFragment : BaseBusinessFragment<FragmentWxBinding>(FragmentWxBinding::inflate) {

    companion object {
        const val TAG_FRAGMENT = "WxFragment"
        fun getInstance(): WxFragment = WxFragment()
    }

    override fun onVisible(isFirstVisible: Boolean) {
        LogUtils.i(TAG_FRAGMENT, "onVisible isFirstVisible:$isFirstVisible")
    }

    override fun onInvisible() {
        LogUtils.i(TAG_FRAGMENT, "onInvisible")
    }

    fun scrollToTop() {
    }

    override fun initView(view: View) {
    }
}