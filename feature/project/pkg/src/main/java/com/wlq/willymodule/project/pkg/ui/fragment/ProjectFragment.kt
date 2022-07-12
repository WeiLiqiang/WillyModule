package com.wlq.willymodule.project.pkg.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessFragment
import com.wlq.willymodule.project.pkg.data.bean.SystemParent
import com.wlq.willymodule.project.pkg.databinding.FragmentProjectBinding
import com.wlq.willymodule.project.pkg.ui.viewmodel.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment :
    BaseBusinessFragment<FragmentProjectBinding, ProjectViewModel>(FragmentProjectBinding::inflate) {

    override val viewModel by lazy { ProjectViewModel() }
    private val mProjectTypeList = mutableListOf<SystemParent>()

    companion object {
        const val TAG_FRAGMENT = "ProjectFragment"
        fun getInstance(): ProjectFragment = ProjectFragment()
    }

    override fun onVisible(isFirstVisible: Boolean) {
        LogUtils.i(TAG_FRAGMENT, "onVisible isFirstVisible:$isFirstVisible")
        if (isFirstVisible) {
            viewModel.getProjectTypeList()
        }
    }

    override fun onInvisible() {
        LogUtils.i(TAG_FRAGMENT, "onInvisible")
    }

    override fun initView(view: View) {
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = mProjectTypeList.size

            override fun createFragment(position: Int): Fragment {
                return ProjectChildFragment.newInstance(mProjectTypeList[position].id)
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = mProjectTypeList[position].name
        }.attach()
    }

    override fun startObserve() {
        super.startObserve()
        viewModel.projectListLiveData.observe(viewLifecycleOwner) {
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