package com.wlq.willymodule.common.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wlq.willymodule.base.mvi.ui.IUiView
import com.wlq.willymodule.base.mvi.observeEvent
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.base.util.ToastUtils
import com.wlq.willymodule.common.utils.livedata.SingleViewEvent
import com.wlq.willymodule.common.view.MultipleStatusView

abstract class BaseBusinessFragment<VB : ViewBinding, out VM : BaseViewModel>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseLazyFragment<VB>(inflate), IUiView {

    protected abstract val viewModel: VM

    protected var mLayoutStatusView: MultipleStatusView? = null

    protected var progressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        onVisible(false)
    }

    abstract fun initView(view: View)

    override fun startObserve() {
        viewModel.apply {

            singleViewEvens.observeEvent(this@BaseBusinessFragment) {
                when (it) {
                    is SingleViewEvent.Toast -> ToastUtils.showShort(it.message)
                    is SingleViewEvent.Log -> LogUtils.log(it.type, this@BaseBusinessFragment::class.java.simpleName, it.message)
                    is SingleViewEvent.ShowLoadingDialog -> showLoading()
                    is SingleViewEvent.DismissLoadingDialog -> dismissLoading()
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