package com.wlq.willymodule.common.base

import android.app.ProgressDialog
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.wlq.willymodule.base.mvi.livedata.SingleViewEvent
import com.wlq.willymodule.base.mvi.observeEvent
import com.wlq.willymodule.base.mvi.viewmodel.BaseViewModel
import com.wlq.willymodule.base.mvi.ui.activity.BaseVBActivity
import com.wlq.willymodule.base.util.BusUtils
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.base.util.ToastUtils
import com.wlq.willymodule.common.R
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.receiver.NetworkChangeReceiver
import com.wlq.willymodule.common.utils.SettingUtil
import com.wlq.willymodule.common.utils.StatusBarUtil

abstract class BaseBusinessActivity<VB : ViewBinding,  VM : BaseBusinessViewModel>(
    inflate: (LayoutInflater) -> VB
) : BaseVBActivity<VB>(inflate) {

    protected abstract val viewModel: VM

    protected var mThemeColor: Int = SettingUtil.getColor()

    protected var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    private var progressDialog: ProgressDialog? = null

    override fun initData(savedInstanceState: Bundle?) {
        viewModel.apply {

            singleViewEvens.observeEvent(this@BaseBusinessActivity) {
                when (it) {
                    is SingleViewEvent.Toast -> ToastUtils.showShort(it.message)
                    is SingleViewEvent.Log -> LogUtils.log(it.type, this::class.java.simpleName, it.message)
                    is SingleViewEvent.ShowLoadingDialog -> showLoading()
                    is SingleViewEvent.DismissLoadingDialog -> dismissLoading()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        BusUtils.register(this)
    }

    override fun onStop() {
        super.onStop()
        BusUtils.unregister(this)
    }

    override fun onResume() {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        mNetworkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(mNetworkChangeReceiver, filter)
        super.onResume()
        initColor()
    }

    override fun onPause() {
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
        super.onPause()
    }

    override fun showLoading() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }

    open fun initColor() {
        mThemeColor = if (!SettingUtil.getIsNightMode()) {
            SettingUtil.getColor()
        } else {
            resources.getColor(R.color.colorPrimary)
        }
        StatusBarUtil.setColor(this, mThemeColor, 0)
        if (this.supportActionBar != null) {
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }

        if (SettingUtil.getNavBar()) {
            window.navigationBarColor = mThemeColor
        } else {
            window.navigationBarColor = Color.BLACK
        }
    }

    protected fun initToolbar(toolbar: Toolbar, homeAsUpEnabled: Boolean, title: String) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}