package com.wlq.willymodule.system.pkg.ui.fragment.knowledge

import com.wlq.willymodule.common.base.recyclerview.BaseBindingAdapter
import com.wlq.willymodule.common.base.recyclerview.BaseVBViewHolder
import com.wlq.willymodule.system.pkg.data.bean.SystemParent
import com.wlq.willymodule.system.pkg.data.rep.transFormSystemChild
import com.wlq.willymodule.system.pkg.databinding.ItemKnowledgeBinding

class KnowledgeAdapter : BaseBindingAdapter<SystemParent, ItemKnowledgeBinding>(ItemKnowledgeBinding::inflate) {

    override fun convert(holder: BaseVBViewHolder, item: SystemParent) {
        holder.getViewBinding<ItemKnowledgeBinding>().apply {
            systemParent.text = item.name
            systemChild.text = transFormSystemChild(item.children)
        }
    }
}