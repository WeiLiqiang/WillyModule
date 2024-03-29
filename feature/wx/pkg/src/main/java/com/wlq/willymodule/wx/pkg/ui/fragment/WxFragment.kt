package com.wlq.willymodule.wx.pkg.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessFragment
import com.wlq.willymodule.wx.pkg.data.bean.SystemParent
import com.wlq.willymodule.wx.pkg.databinding.FragmentWxBinding
import com.wlq.willymodule.wx.pkg.ui.viewmodel.WxViewModel
import kotlinx.android.synthetic.main.fragment_wx.*

class WxFragment :
    BaseBusinessFragment<FragmentWxBinding, WxViewModel>(FragmentWxBinding::inflate) {

    override val viewModel by lazy { WxViewModel() }
    private val mProjectTypeList = mutableListOf<SystemParent>()

    companion object {
        const val TAG_FRAGMENT = "WxFragment"
        fun getInstance(): WxFragment = WxFragment()
    }

    override fun onVisible(isFirstVisible: Boolean) {
        LogUtils.i(TAG_FRAGMENT, "onVisible isFirstVisible:$isFirstVisible")
        if (isFirstVisible) {
            viewModel.getBolgTypeList()
        }
    }

    override fun onInvisible() {
        LogUtils.i(TAG_FRAGMENT, "onInvisible")
    }

    override fun initView(view: View) {
        initViewPager()
    }

    private fun initViewPager() {
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = mProjectTypeList.size

            override fun createFragment(position: Int): Fragment {
                return WxChildFragment.newInstance(mProjectTypeList[position].id)
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = mProjectTypeList[position].name
        }.attach()
    }

    override fun startObserve() {
        super.startObserve()
        viewModel.blogListLiveData.observe(viewLifecycleOwner) {
            it?.run { getProjectTypeList(it) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getProjectTypeList(projectTypeList: List<SystemParent>) {
        mProjectTypeList.clear()
        mProjectTypeList.addAll(projectTypeList)
        viewPager.adapter?.notifyDataSetChanged()
        viewPager.offscreenPageLimit = mProjectTypeList.size
    }
}