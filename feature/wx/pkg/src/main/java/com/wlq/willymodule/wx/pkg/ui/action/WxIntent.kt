package com.wlq.willymodule.wx.pkg.ui.action

import com.wlq.willymodule.base.mvi.IViewIntent

sealed class WxIntent : IViewIntent {

    data class RefreshListData(val isRefresh: Boolean, val cid: Int) : WxIntent()

    data class CollectArticle(val collectId: Int, val collect: Boolean) : WxIntent()
}