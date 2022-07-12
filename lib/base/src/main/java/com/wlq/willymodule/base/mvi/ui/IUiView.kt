package com.wlq.willymodule.base.mvi.ui

import androidx.lifecycle.LifecycleOwner

interface IUiView : LifecycleOwner{

    fun showLoading()

    fun dismissLoading()
}