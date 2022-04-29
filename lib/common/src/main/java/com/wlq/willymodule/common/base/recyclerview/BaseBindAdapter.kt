package com.wlq.willymodule.common.base.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule

abstract class BaseBindingAdapter<T, VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    layoutResId: Int = -1
) : BaseQuickAdapter<T, BaseVBViewHolder>(layoutResId), LoadMoreModule {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        BaseVBViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))
}