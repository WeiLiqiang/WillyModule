package com.wlq.willymodule.system.pkg.ui.fragment.square

import com.wlq.willymodule.common.base.recyclerview.BaseBindingAdapter
import com.wlq.willymodule.common.base.recyclerview.BaseVBViewHolder
import com.wlq.willymodule.system.pkg.data.bean.Article
import com.wlq.willymodule.system.pkg.databinding.ItemSquareConstraintBinding

class SquareAdapter : BaseBindingAdapter<Article, ItemSquareConstraintBinding>(ItemSquareConstraintBinding::inflate) {

    override fun convert(holder: BaseVBViewHolder, item: Article) {
        holder.getViewBinding<ItemSquareConstraintBinding>().apply {
            shareTitle.text = item.title
            shareTime.text = item.niceDate
            shareAuthor.text = item.author.ifBlank { "分享者: ${item.shareUser}" }
        }
    }
}