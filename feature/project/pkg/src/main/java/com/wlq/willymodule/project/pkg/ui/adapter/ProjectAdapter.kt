package com.wlq.willymodule.project.pkg.ui.adapter

import com.wlq.willymodule.base.business.glide.GlideImageLoader
import com.wlq.willymodule.base.base.recyclerview.BaseBindingAdapter
import com.wlq.willymodule.base.base.recyclerview.BaseVBViewHolder
import com.wlq.willymodule.project.pkg.R
import com.wlq.willymodule.project.pkg.data.bean.ProjectDetail
import com.wlq.willymodule.project.pkg.databinding.ItemProjectBinding

class ProjectAdapter : BaseBindingAdapter<ProjectDetail, ItemProjectBinding>(ItemProjectBinding::inflate) {

    override fun convert(holder: BaseVBViewHolder, item: ProjectDetail) {
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