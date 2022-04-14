package com.wlq.willymodule.index.pkg.ui

import android.annotation.SuppressLint
import android.view.View
import com.wlq.willymodule.base.image.glide.GlideImageLoader
import com.wlq.willymodule.common.base.recyclerview.BaseBindingAdapter
import com.wlq.willymodule.common.base.recyclerview.BaseVBViewHolder
import com.wlq.willymodule.index.pkg.data.bean.Article
import com.wlq.willymodule.launcher.mock.pkg.R
import com.wlq.willymodule.launcher.mock.pkg.databinding.ItemHomeListBinding

class HomeAdapter : BaseBindingAdapter<Article, ItemHomeListBinding>(ItemHomeListBinding::inflate) {

    private var showStar = true

    fun showStar(showStar: Boolean) {
        this.showStar = showStar
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseVBViewHolder, item: Article) {
        holder.getViewBinding<ItemHomeListBinding>().apply {
            addChildClickViewIds(R.id.iv_like)
            tvArticleAuthor.text = item.author
            if (showStar) {
                ivLike.setImageResource(if (item.collect) R.drawable.timeline_like_pressed else R.drawable.timeline_like_normal)
            } else {
                ivLike.visibility = View.GONE
            }
            tvArticleAuthor.text = item.author.ifBlank { "分享者: ${item.shareUser}" }
            tvArticleTitle.text = item.title
            tvArticleTag.text = "${item.superChapterName ?: ""} ${item.chapterName}"
            tvArticleDate.text = item.niceDate
            GlideImageLoader.getInstance().displayImage(context, item.envelopePic, ivArticleThumbnail)
        }
    }
}