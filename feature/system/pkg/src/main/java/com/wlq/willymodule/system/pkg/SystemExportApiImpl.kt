package com.wlq.willymodule.system.pkg

import androidx.fragment.app.FragmentTransaction
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.system.export.SystemExportApi
import com.wlq.willymodule.system.pkg.ui.SystemFragment

@ApiUtils.Api
class SystemExportApiImpl : SystemExportApi() {

    private var systemFragment: SystemFragment? = null

    override fun showFragment(transaction: FragmentTransaction, containerViewId: Int) {
        if (systemFragment == null) {
            systemFragment = SystemFragment.getInstance()
            transaction.add(containerViewId, systemFragment!!, SystemFragment.TAG_FRAGMENT)
        } else {
            transaction.show(systemFragment!!)
        }
    }

    override fun hideFragment(transaction: FragmentTransaction) {
        if (systemFragment == null) {
            return
        }
        transaction.hide(systemFragment!!)
    }

    override fun removeFragment(transaction: FragmentTransaction) {
        if (systemFragment == null) {
            return
        }
        transaction.remove(systemFragment!!)
    }

    override fun destroy() {
        systemFragment = null
    }
}