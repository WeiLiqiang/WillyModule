package com.wlq.willymodule.common.base.viewpager2

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wlq.willymodule.base.base.BaseLazy2Fragment
import com.wlq.willymodule.base.mvi.ui.IUiView
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.base.util.ToastUtils
import com.wlq.willymodule.base.mvi.viewmodel.BaseViewModel
import com.wlq.willymodule.base.mvi.livedata.CommonViewEvent
import com.wlq.willymodule.base.mvi.observeEvent
import com.wlq.willymodule.common.view.MultipleStatusView

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
        viewModel.apply {
            singleViewEvens.observeEvent(viewLifecycleOwner) {
                when (it) {
                    is CommonViewEvent.Toast -> ToastUtils.showShort(it.message)
                    is CommonViewEvent.Log -> LogUtils.log(it.type, this@BaseBusiness2Fragment::class.java.simpleName, it.message)
                    is CommonViewEvent.ShowLoadingDialog -> showLoading()
                    is CommonViewEvent.DismissLoadingDialog -> dismissLoading()
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