package com.wlq.willymodule.base.mvi.view

import androidx.lifecycle.LifecycleOwner

interface IUiView : LifecycleOwner{

    fun showLoading()

    fun dismissLoading()
}