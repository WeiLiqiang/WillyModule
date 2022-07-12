package com.wlq.willymodule.system.pkg.ui.fragment.systemtype

import com.wlq.willymodule.base.base.recyclerview.BaseBindingAdapter
import com.wlq.willymodule.base.base.recyclerview.BaseVBViewHolder
import com.wlq.willymodule.system.pkg.R
import com.wlq.willymodule.system.pkg.data.bean.Article
import com.wlq.willymodule.system.pkg.databinding.ItemArticleConstraintBinding

class SystemArticleAdapter : BaseBindingAdapter<Article, ItemArticleConstraintBinding>(ItemArticleConstraintBinding::inflate) {

    override fun convert(holder: BaseVBViewHolder, item: Article) {
        holder.getViewBinding<ItemArticleConstraintBinding>().apply {
            addChildClickViewIds(R.id.articleStar)
            articleStar.setImageResource(if (item.collect) R.drawable.ic_like_pressed else R.drawable.ic_like_normal)
            articleTag.text = "${item.superChapterName}/${item.chapterName}"
            articleTitle.text = item.title
            articleTime.text = item.niceDate
            articleAuthor.text = item.author.ifBlank { "分享者: ${item.shareUser}" }
        }
    }
}