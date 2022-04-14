package com.wlq.willymodule.index.pkg.ui

import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.index.pkg.data.bean.Article

data class IndexCollectViewState(
    val collectArticleList: ApiPageResponse<Article>? = null
)

data class IndexListViewState(
    val listStatus: ListStatus = ListStatus.Loading,
    val articleList: Pair<IsRefresh, ApiPageResponse<Article>?>? = null,
)

sealed class IsRefresh {
    object TRUE : IsRefresh()
    object FALSE : IsRefresh()
}

sealed class ListStatus {
    object Loading : ListStatus()
    object Success : ListStatus()
    object Error : ListStatus()
    object LoadMoreEnd : ListStatus()
}



