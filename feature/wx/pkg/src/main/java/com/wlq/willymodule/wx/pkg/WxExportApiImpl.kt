package com.wlq.willymodule.wx.pkg

import androidx.fragment.app.FragmentTransaction
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.wx.export.WxExportApi
import com.wlq.willymodule.wx.pkg.ui.WxFragment

@ApiUtils.Api
class WxExportApiImpl : WxExportApi() {

    private var wxFragment: WxFragment? = null

    override fun showFragment(transaction: FragmentTransaction?, containerViewId: Int) {
        if (wxFragment == null) {
            wxFragment = WxFragment.getInstance()
            transaction?.add(containerViewId, wxFragment!!, WxFragment.TAG_FRAGMENT)
        } else {
            transaction?.show(wxFragment!!)
        }
    }

    override fun hideFragment(transaction: FragmentTransaction?) {
        if (wxFragment == null) {
            return
        }
        transaction?.hide(wxFragment!!)
    }

    override fun removeFragment(transaction: FragmentTransaction?) {
        if (wxFragment == null) {
            return
        }
        transaction?.remove(wxFragment!!)
    }

    override fun destroy() {
        wxFragment = null
    }
}