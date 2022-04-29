package com.wlq.willymodule.index.pkg

import androidx.fragment.app.FragmentTransaction
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.index.export.IndexExportApi
import com.wlq.willymodule.index.pkg.ui.IndexFragment

@ApiUtils.Api
class IndexExportApiImpl : IndexExportApi() {

    private var indexFragment: IndexFragment? = null

    override fun showFragment(
        transaction: FragmentTransaction?,
        containerViewId: Int
    ) {
        if (indexFragment == null) {
            indexFragment = IndexFragment.getInstance()
            transaction?.add(containerViewId, indexFragment!!, IndexFragment.TAG_FRAGMENT)
        } else {
            transaction?.show(indexFragment!!)
        }
    }

    override fun hideFragment(transaction: FragmentTransaction?) {
        if (indexFragment == null) {
            return
        }
        transaction?.hide(indexFragment!!)
    }

    override fun removeFragment(transaction: FragmentTransaction?) {
        if (indexFragment == null) {
            return
        }
        transaction?.remove(indexFragment!!)
    }

    override fun fragmentScrollToTop() {
        indexFragment?.scrollToTop()
    }

    override fun destroy() {
        indexFragment = null
    }
}