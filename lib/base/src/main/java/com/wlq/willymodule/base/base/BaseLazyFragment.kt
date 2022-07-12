package com.wlq.willymodule.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.wlq.willymodule.base.mvi.ui.fragment.BaseVBFragment

abstract class BaseLazyFragment<VB : ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseVBFragment<VB>(inflate) {

    private var mIsFirstVisible = true
    private var mUserVisible = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (mViewCreated) {
            if (isVisibleToUser && !mUserVisible) {
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && mUserVisible) {
                dispatchUserVisibleHint(false)
            }
        }
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!isHidden && userVisibleHint) {
            dispatchUserVisibleHint(true)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mIsFirstVisible) {
            if (!isHidden && !mUserVisible && userVisibleHint) {
                dispatchUserVisibleHint(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mUserVisible && userVisibleHint) {
            dispatchUserVisibleHint(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsFirstVisible = true
    }

    private fun dispatchUserVisibleHint(visible: Boolean) {
        if (visible && !isParentVisible()) {
            return
        }
        if (mUserVisible == visible) {
            return
        }
        mUserVisible = visible
        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                onVisible(true)
            } else {
                onVisible(false)
            }
            dispatchChildVisibleState(true)
        } else {
            dispatchChildVisibleState(false)
            onInvisible()
        }
    }

    open fun dispatchChildVisibleState(visible: Boolean) {
        val childFragmentManager: FragmentManager = childFragmentManager
        val fragments: List<Fragment> = childFragmentManager.fragments
        if (fragments.isNotEmpty()) {
            for (child in fragments) {
                if (child is BaseLazyFragment<*> && !child.isHidden && child.getUserVisibleHint()) {
                    child.dispatchUserVisibleHint(visible)
                }
            }
        }
    }

    open fun isParentVisible(): Boolean {
        val fragment = parentFragment ?: return true
        if (fragment is BaseLazyFragment<*>) {
            val lazyFragment: BaseLazyFragment<*> = fragment
            return lazyFragment.isSupportUserVisible()
        }
        return fragment.isVisible
    }

    open fun isSupportUserVisible(): Boolean {
        return mUserVisible
    }

    protected open fun onVisible(isFirstVisible: Boolean) {}

    protected open fun onInvisible() {}
}