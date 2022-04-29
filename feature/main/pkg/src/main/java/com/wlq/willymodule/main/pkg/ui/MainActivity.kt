package com.wlq.willymodule.main.pkg.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.base.util.AppUtils
import com.wlq.willymodule.base.util.ClickUtils
import com.wlq.willymodule.base.util.ClickUtils.Back2HomeFriendlyListener
import com.wlq.willymodule.base.util.ToastUtils
import com.wlq.willymodule.common.base.BaseBusinessActivity
import com.wlq.willymodule.common.databinding.LayoutToolbarBinding
import com.wlq.willymodule.common.utils.Preference
import com.wlq.willymodule.index.export.IndexExportApi
import com.wlq.willymodule.main.pkg.R
import com.wlq.willymodule.main.pkg.data.MainPkgConstants
import com.wlq.willymodule.main.pkg.databinding.ActivityMainBinding
import com.wlq.willymodule.navigation.export.NavigationExportApi
import com.wlq.willymodule.project.export.ProjectExportApi
import com.wlq.willymodule.system.export.SystemExportApi
import com.wlq.willymodule.wx.export.WxExportApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseBusinessActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val BOTTOM_INDEX: String = "bottom_index"
    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_SYSTEM = 0x02
    private val FRAGMENT_NAVIGATION = 0x03
    private val FRAGMENT_PROJECT = 0x04
    private val FRAGMENT_WECHAT = 0x05
    private var mIndex = FRAGMENT_HOME

    /**
     * check login
     */
    protected var isLogin: Boolean by Preference(MainPkgConstants.LOGIN_KEY, false)

    /**
     * local username
     */
    private val username: String by Preference(MainPkgConstants.USERNAME_KEY, "")

    /**
     * username TextView
     */
    private var nav_username: TextView? = null
    private lateinit var layoutToolbarBinding: LayoutToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(BOTTOM_INDEX)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_INDEX, mIndex)
    }

    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            ApiUtils.getApi(IndexExportApi::class.java)?.removeFragment(fragmentTransaction)
            ApiUtils.getApi(SystemExportApi::class.java)?.removeFragment(fragmentTransaction)
            ApiUtils.getApi(NavigationExportApi::class.java)?.removeFragment(fragmentTransaction)
            ApiUtils.getApi(ProjectExportApi::class.java)?.removeFragment(fragmentTransaction)
            ApiUtils.getApi(WxExportApi::class.java)?.removeFragment(fragmentTransaction)
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }

    override fun doBusiness() {
        initView()
        initData()
    }

    private fun initView() {
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
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        nav_username?.run {
            text = if (!isLogin) {
                getString(R.string.login)
            } else {
                username
            }
            setOnClickListener {
                if (!isLogin) {
                } else {

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

    private fun initData() {

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
                    if (isLogin) {
                    } else {
                    }
                }
                R.id.nav_setting -> {
                }
                R.id.nav_about_us -> {
                }
                R.id.nav_logout -> {
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