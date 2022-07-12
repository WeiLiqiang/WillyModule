package com.wlq.willymodule.base.base.recyclerview

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class BaseVBViewHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
    constructor(itemView: View) : this(ViewBinding { itemView })

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getViewBinding() = binding as VB
}