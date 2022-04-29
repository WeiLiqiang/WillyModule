package com.wlq.willymodule.system.pkg.ui.activity

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.wlq.willymodule.common.base.BaseBusinessActivity
import com.wlq.willymodule.system.pkg.R
import com.wlq.willymodule.system.pkg.data.bean.SystemParent
import com.wlq.willymodule.system.pkg.databinding.ActivitySystemDetailBinding
import com.wlq.willymodule.system.pkg.ui.fragment.systemtype.SystemTypeFragment
import kotlinx.android.synthetic.main.activity_system_detail.*

class SystemTypeNormalActivity :
    BaseBusinessActivity<ActivitySystemDetailBinding>(ActivitySystemDetailBinding::inflate) {

    companion object {
        const val ARTICLE_LIST = "article_list"
    }
    private val systemParent: SystemParent by lazy { intent.getSerializableExtra(ARTICLE_LIST) as SystemParent }

    override fun doBusiness() {
        initView()
        initData()
    }

    private fun initView() {
        mToolbar.run {
            title = systemParent.name
            setNavigationIcon(R.drawable.ic_arrow_back)
        }
        systemDetailViewPager.offscreenPageLimit = systemParent.children.size
        systemDetailViewPager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = systemParent.children.size

            override fun createFragment(position: Int) =
                SystemTypeFragment.newInstance(systemParent.children[position].id, false)
        }

        TabLayoutMediator(tabLayout, systemDetailViewPager) { tab, position ->
            tab.text = systemParent.children[position].name
        }.attach()
    }

    private fun initData() {
        mToolbar.setNavigationOnClickListener { onBackPressed() }
    }
}