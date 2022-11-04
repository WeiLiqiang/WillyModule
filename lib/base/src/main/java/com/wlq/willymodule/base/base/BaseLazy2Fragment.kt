package com.wlq.willymodule.base.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wlq.willymodule.base.mvi.view.fragment.BaseVBFragment

abstract class BaseLazy2Fragment<VB : ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseVBFragment<VB>(inflate) {

    private var visibleToUser: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!visibleToUser) {
            visibleToUser = true
            firstVisibleToUser()
        }
    }

    protected open fun firstVisibleToUser() {}
}