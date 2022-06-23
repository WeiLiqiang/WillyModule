package com.wlq.willymodule.wx.pkg.ui.fragment

import com.wlq.willymodule.base.base.recyclerview.BaseBindingAdapter
import com.wlq.willymodule.base.base.recyclerview.BaseVBViewHolder
import com.wlq.willymodule.wx.pkg.R
import com.wlq.willymodule.wx.pkg.data.bean.Article
import com.wlq.willymodule.wx.pkg.databinding.ItemWxChildBinding

class WxChildAdapter : BaseBindingAdapter<Article, ItemWxChildBinding>(ItemWxChildBinding::inflate) {

    override fun convert(holder: BaseVBViewHolder, item: Article) {
        holder.getViewBinding<ItemWxChildBinding>().apply {
            articleTitle.text = item.title
            articleTime.text = item.niceDate
            articleTag.text = item.tags.toString()
            articleAuthor.text = item.author.ifBlank { "分享者: ${item.shareUser}" }
            articleStar.setImageResource(if (item.collect) R.drawable.ic_like_pressed else R.drawable.ic_like_normal)
        }
    }
}