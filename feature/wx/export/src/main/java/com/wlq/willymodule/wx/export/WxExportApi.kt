package com.wlq.willymodule.wx.export

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentTransaction
import com.wlq.willymodule.base.util.ApiUtils.BaseApi

abstract class WxExportApi : BaseApi() {
    abstract fun showFragment(transaction: FragmentTransaction?, @IdRes containerViewId: Int)
    abstract fun hideFragment(transaction: FragmentTransaction?)
    abstract fun removeFragment(transaction: FragmentTransaction?)
    abstract fun destroy()
}