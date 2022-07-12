package com.wlq.willymodule.system.pkg.ui

import android.view.View
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessFragment
import com.wlq.willymodule.system.pkg.databinding.FragmentSystemBinding
import com.wlq.willymodule.system.pkg.ui.fragment.knowledge.KnowledgeFragment
import com.wlq.willymodule.system.pkg.ui.fragment.project.ProjectFragment
import com.wlq.willymodule.system.pkg.ui.fragment.square.SquareFragment
import kotlinx.android.synthetic.main.fragment_system.*

class SystemFragment :
    BaseBusinessFragment<FragmentSystemBinding, SystemViewModel>(FragmentSystemBinding::inflate) {

    override val viewModel by lazy { SystemViewModel() }
    private val titleList = arrayOf("广场", "体系", "最新项目")
    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    companion object {
        const val TAG_FRAGMENT = "SystemFragment"
        fun getInstance(): SystemFragment = SystemFragment()
    }

    override fun onVisible(isFirstVisible: Boolean) {
        LogUtils.i(TAG_FRAGMENT, "onVisible isFirstVisible:$isFirstVisible")
    }

    override fun onInvisible() {
        LogUtils.i(TAG_FRAGMENT, "onInvisible")
    }

    override fun initView(view: View) {
        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int) = when (position) {
                0 -> SquareFragment.getInstance()
                1 -> KnowledgeFragment.getInstance()
                2 -> ProjectFragment.getInstance(0, true)
                else -> SquareFragment.getInstance()
            }

            override fun getItemCount() = titleList.size
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        if (onPageChangeCallback == null) onPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    if (position == 1) {

                    } else {

                    }
                }
            }
        onPageChangeCallback?.let {
            viewPager.registerOnPageChangeCallback(it)
        }
    }

    override fun onPause() {
        super.onPause()
        onPageChangeCallback?.let {
            viewPager.unregisterOnPageChangeCallback(it)
        }
    }
}