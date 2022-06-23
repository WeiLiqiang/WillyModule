package com.wlq.willymodule.system.pkg.ui.fragment.project

import com.wlq.willymodule.base.business.glide.GlideImageLoader
import com.wlq.willymodule.base.base.recyclerview.BaseBindingAdapter
import com.wlq.willymodule.base.base.recyclerview.BaseVBViewHolder
import com.wlq.willymodule.system.pkg.R
import com.wlq.willymodule.system.pkg.data.bean.Article
import com.wlq.willymodule.system.pkg.databinding.ItemProjectBinding

class ProjectAdapter : BaseBindingAdapter<Article, ItemProjectBinding>(ItemProjectBinding::inflate) {

    override fun convert(holder: BaseVBViewHolder, item: Article) {
        holder.getViewBinding<ItemProjectBinding>().apply {
            GlideImageLoader.getInstance().displayImage(context, item.envelopePic, projectImg)
            projectName.text = item.title
            projectDesc.text = item.desc
            projectTime.text = item.niceDate
            projectAuthor.text = item.author
            articleStar.setImageResource(if (item.collect) R.drawable.ic_like_pressed else R.drawable.ic_like_normal)
        }
    }
}