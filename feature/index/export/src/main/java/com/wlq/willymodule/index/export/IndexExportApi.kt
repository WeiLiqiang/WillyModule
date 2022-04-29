package com.wlq.willymodule.index.export

import com.wlq.willymodule.base.util.ApiUtils.BaseApi
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentTransaction

abstract class IndexExportApi : BaseApi() {
    abstract fun showFragment(transaction: FragmentTransaction?, @IdRes containerViewId: Int)
    abstract fun hideFragment(transaction: FragmentTransaction?)
    abstract fun removeFragment(transaction: FragmentTransaction?)
    abstract fun fragmentScrollToTop()
    abstract fun destroy()
}