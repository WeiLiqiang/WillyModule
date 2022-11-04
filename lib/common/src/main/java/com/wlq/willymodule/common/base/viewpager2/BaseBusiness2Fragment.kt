package com.wlq.willymodule.common.base.viewpager2

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.wlq.willymodule.base.base.BaseLazy2Fragment
import com.wlq.willymodule.base.mvi.intent.SingleEvent
import com.wlq.willymodule.base.mvi.view.IUiView
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.base.mvi.vm.BaseViewModel
import com.wlq.willymodule.base.util.Utils
import com.wlq.willymodule.common.view.MultipleStatusView
import kotlinx.coroutines.flow.collect

abstract class BaseBusiness2Fragment<VB : ViewBinding, out VM : BaseViewModel>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseLazy2Fragment<VB>(inflate), IUiView {

    protected abstract val viewModel: VM

    protected var mLayoutStatusView: MultipleStatusView? = null

    protected var progressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {

    }

    override fun startObserve() {
        lifecycleScope.launchWhenStarted {
            viewModel.singleEvent.collect {
                when(it) {
                    is SingleEvent.ShowToast -> {
                        Toast.makeText(Utils.getApp(), it.content, Toast.LENGTH_SHORT).show()
                    }
                    is SingleEvent.ShowLog -> {
                        LogUtils.log(it.type, this::class.java.simpleName, it.content)
                    }
                    is SingleEvent.ShowCommonDialog -> {
                        //TODO 显示通用弹框
                    }
                    is SingleEvent.ShowLoadingDialog -> {
                        showLoading()
                    }
                    is SingleEvent.DismissLoadingDialog -> {
                        dismissLoading()
                    }
                }
            }
        }
    }

    override fun showLoading() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(context)
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }
}