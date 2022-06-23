package com.wlq.willymodule.project.pkg.ui.action

import com.wlq.willymodule.base.mvi.IViewIntent

sealed class ProjectIntent : IViewIntent {

    data class RefreshListData(val isRefresh: Boolean, val cid: Int) : ProjectIntent()

    data class CollectArticle(val collectId: Int, val collect: Boolean) : ProjectIntent()
}