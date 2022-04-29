package com.wlq.willymodule.navigation.pkg.ui

import android.view.View
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessFragment
import com.wlq.willymodule.navigation.pkg.databinding.FragmentNavigationBinding

class NavigationFragment : BaseBusinessFragment<FragmentNavigationBinding, NavigationViewModel>(FragmentNavigationBinding::inflate) {

    override val viewModel by lazy { NavigationViewModel() }

    companion object {
        const val TAG_FRAGMENT = "NavigationFragment"
        fun getInstance(): NavigationFragment = NavigationFragment()
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