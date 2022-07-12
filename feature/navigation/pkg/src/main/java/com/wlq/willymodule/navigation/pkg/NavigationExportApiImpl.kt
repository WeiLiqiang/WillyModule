package com.wlq.willymodule.navigation.pkg

import androidx.fragment.app.FragmentTransaction
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.navigation.export.NavigationExportApi
import com.wlq.willymodule.navigation.pkg.ui.NavigationFragment

@ApiUtils.Api
class NavigationExportApiImpl : NavigationExportApi() {

    private var navigationFragment: NavigationFragment? = null

    override fun showFragment(transaction: FragmentTransaction?, containerViewId: Int) {
        if (navigationFragment == null) {
            navigationFragment = NavigationFragment.getInstance()
            transaction?.add(containerViewId, navigationFragment!!, NavigationFragment.TAG_FRAGMENT)
        } else {
            transaction?.show(navigationFragment!!)
        }
    }

    override fun hideFragment(transaction: FragmentTransaction?) {
        if (navigationFragment == null) {
            return
        }
        transaction?.hide(navigationFragment!!)
    }

    override fun removeFragment(transaction: FragmentTransaction?) {
        if (navigationFragment == null) {
            return
        }
        transaction?.remove(navigationFragment!!)
    }

    override fun destroy() {
        navigationFragment = null
    }

}