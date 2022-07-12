package com.wlq.willymodule.main.pkg.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import com.wlq.willymodule.base.business.glide.GlideImageLoader
import com.wlq.willymodule.base.mvi.observeState
import com.wlq.willymodule.base.util.*
import com.wlq.willymodule.base.util.ClickUtils.Back2HomeFriendlyListener
import com.wlq.willymodule.common.base.BaseBusinessActivity
import com.wlq.willymodule.common.databinding.LayoutToolbarBinding
import com.wlq.willymodule.common.model.bean.UserInfo
import com.wlq.willymodule.common.view.CircleImageView
import com.wlq.willymodule.index.export.IndexExportApi
import com.wlq.willymodule.main.pkg.R
import com.wlq.willymodule.main.pkg.data.constant.MainConstants
import com.wlq.willymodule.main.pkg.databinding.ActivityMainBinding
import com.wlq.willymodule.main.pkg.ui.action.MainViewLoginState
import com.wlq.willymodule.main.pkg.ui.viewmodel.MainViewModel
import com.wlq.willymodule.navigation.export.NavigationExportApi
import com.wlq.willymodule.project.export.ProjectExportApi
import com.wlq.willymodule.system.export.SystemExportApi
import com.wlq.willymodule.wx.export.WxExportApi
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity :
    BaseBusinessActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {

    override val viewModel: MainViewModel by viewModels()

    private val BOTTOM_INDEX: String = "bottom_index"
    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_SYSTEM = 0x02
    private val FRAGMENT_NAVIGATION = 0x03
    private val FRAGMENT_PROJECT = 0x04
    private val FRAGMENT_WECHAT = 0x05
    private var mIndex = FRAGMENT_HOME

    private var tvNavUsername: TextView? = null
    private var ivNavHeader: CircleImageView? = null
    private var itemLogOut: MenuItem? = null
    private lateinit var layoutToolbarBinding: LayoutToolbarBinding

    @BusUtils.Bus(tag = MainConstants.BUS_TAG_USER_NAME, sticky = true)
    fun loginSuccessAndUpdateUserInfo(userInfo: UserInfo) {
        viewModel.updateUserInfo(userInfo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_INDEX, mIndex)
    }

    @SuppressLint("RtlHardcoded")
    override fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(BOTTOM_INDEX)
        }
        viewModel.getUserInfo()?.let { viewModel.updateUserInfo(it) }
        viewModel.apply {
            viewLoginState.run {
                observeState(this@MainActivity, MainViewLoginState::username) {
                    tvNavUsername?.text = it
                }
                observeState(this@MainActivity, MainViewLoginState::headerUrl) {
                    GlideImageLoader.getInstance().displayImage(Utils.getApp(), it, ivNavHeader)
                }
                observeState(this@MainActivity, MainViewLoginState::loginSuccess) {
                    if (drawer_layout.isDrawerOpen(Gravity.LEFT)) drawer_layout.closeDrawers()
                    GlideImageLoader.getInstance()
                        .displayImage(Utils.getApp(), R.mipmap.ic_default_avatar, ivNavHeader)
                    itemLogOut?.isVisible = it
                }
            }
        }
    }

    override fun initView() {
        layoutToolbarBinding = LayoutToolbarBinding.bind(binding.root)
        layoutToolbarBinding.toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }
        bottom_navigation_view.run {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }
        initDrawerLayout()
        navigation_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            val headerView = getHeaderView(0)
            headerView.setOnClickListener {
                if (!viewModel.isLogin()) {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            }
            tvNavUsername = headerView.findViewById(R.id.tv_username)
            ivNavHeader = headerView.findViewById(R.id.civ_header)
            itemLogOut = menu.findItem(R.id.nav_logout);
        }
        tvNavUsername?.run {
            text = if (!viewModel.isLogin()) {
                getString(R.string.login)
            } else {
                viewModel.getUserInfo()?.nickname
            }
            setOnClickListener {
                if (checkLoginAccount()) {

                }
            }
        }
        showFragment(mIndex)
        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    private fun initDrawerLayout() {
        drawer_layout.run {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                layoutToolbarBinding.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    /**
     * 展示Fragment
     * @param index
     */
    private fun showFragment(index: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        hideFragments(fragmentTransaction)
        mIndex = index
        when (index) {
            FRAGMENT_HOME // 首页
            -> {
                layoutToolbarBinding.toolbar.title = getString(R.string.app_name)
                ApiUtils.getApi(IndexExportApi::class.java)
                    ?.showFragment(fragmentTransaction, R.id.container)
            }
            FRAGMENT_SYSTEM // 体系
            -> {
                layoutToolbarBinding.toolbar.title = getString(R.string.knowledge_system)
                ApiUtils.getApi(SystemExportApi::class.java)
                    ?.showFragment(fragmentTransaction, R.id.container)
            }
            FRAGMENT_NAVIGATION // 导航
            -> {
                layoutToolbarBinding.toolbar.title = getString(R.string.navigation)
                ApiUtils.getApi(NavigationExportApi::class.java)
                    ?.showFragment(fragmentTransaction, R.id.container)
            }
            FRAGMENT_PROJECT // 项目
            -> {
                layoutToolbarBinding.toolbar.title = getString(R.string.project)
                ApiUtils.getApi(ProjectExportApi::class.java)
                    ?.showFragment(fragmentTransaction, R.id.container)
            }
            FRAGMENT_WECHAT // 公众号
            -> {
                layoutToolbarBinding.toolbar.title = getString(R.string.wechat)
                ApiUtils.getApi(WxExportApi::class.java)
                    ?.showFragment(fragmentTransaction, R.id.container)
            }
        }
        fragmentTransaction.commit()
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(fragmentTransaction: FragmentTransaction) {
        ApiUtils.getApi(IndexExportApi::class.java)?.hideFragment(fragmentTransaction)
        ApiUtils.getApi(SystemExportApi::class.java)?.hideFragment(fragmentTransaction)
        ApiUtils.getApi(NavigationExportApi::class.java)?.hideFragment(fragmentTransaction)
        ApiUtils.getApi(ProjectExportApi::class.java)?.hideFragment(fragmentTransaction)
        ApiUtils.getApi(WxExportApi::class.java)?.hideFragment(fragmentTransaction)
    }

    /**
     * NavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_collect -> {
                    if (checkLoginAccount()) {

                    }
                }
                R.id.nav_setting -> {
                }
                R.id.nav_about_us -> {
                }
                R.id.nav_logout -> {
                    viewModel.logout()
                }
                R.id.nav_night_mode -> {
                    window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                }
                R.id.nav_todo -> {
                }
            }
            true
        }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            return@OnNavigationItemSelectedListener when (item.itemId) {
                R.id.action_home -> {
                    showFragment(FRAGMENT_HOME)
                    true
                }
                R.id.action_system -> {
                    showFragment(FRAGMENT_SYSTEM)
                    true
                }
                R.id.action_navigation -> {
                    showFragment(FRAGMENT_NAVIGATION)
                    true
                }
                R.id.action_project -> {
                    showFragment(FRAGMENT_PROJECT)
                    true
                }
                R.id.action_wechat -> {
                    showFragment(FRAGMENT_WECHAT)
                    true
                }
                else -> {
                    false
                }
            }
        }

    /**
     * FAB 监听
     */
    private val onFABClickListener = View.OnClickListener {
        when (mIndex) {
            FRAGMENT_HOME -> {
                ApiUtils.getApi(IndexExportApi::class.java)?.fragmentScrollToTop()
            }
            FRAGMENT_SYSTEM -> {
            }
            FRAGMENT_NAVIGATION -> {
            }
            FRAGMENT_PROJECT -> {
            }
            FRAGMENT_WECHAT -> {
            }
        }
    }

    private fun checkLoginAccount(): Boolean {
        if (!viewModel.isLogin()) {
            ActivityUtils.startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
        return viewModel.isLogin()
    }

    override fun onBackPressed() {
        ClickUtils.back2HomeFriendly("再点击一次退出应用", 2000L, object : Back2HomeFriendlyListener {
            override fun show(text: CharSequence?, duration: Long) {
                ToastUtils.showShort(text)
            }

            override fun dismiss() {
                AppUtils.exitApp()
            }
        })
    }
}