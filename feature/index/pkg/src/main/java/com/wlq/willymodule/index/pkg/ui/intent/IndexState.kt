package com.wlq.willymodule.index.pkg.ui.intent

import com.wlq.willymodule.base.mvi.intent.UiMultipleEvent
import com.wlq.willymodule.base.mvi.intent.UiState
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.index.pkg.data.bean.IndexArticle

data class IndexCollectViewState(
    val collectArticleList: ApiPageResponse<IndexArticle>? = null
) : UiState

data class IndexListViewState(
    val listStatus: ListStatus = ListStatus.Loading,
    val articleList: Pair<IsRefresh, ApiPageResponse<IndexArticle>?>? = null,
) : UiState

sealed class IndexEvent : UiMultipleEvent {

}





