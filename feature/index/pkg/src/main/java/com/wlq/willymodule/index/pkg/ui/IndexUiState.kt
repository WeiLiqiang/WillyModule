package com.wlq.willymodule.index.pkg.ui

import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.index.pkg.data.bean.Article
import com.wlq.willymodule.index.pkg.data.bean.Banner

data class IndexCollectViewState(
    val collectArticleList: ApiPageResponse<Article>? = null
)

data class IndexListViewState(
    val listStatus: ListStatus = ListStatus.Loading,
    val articleList: Pair<IsRefresh, ApiPageResponse<Article>?>? = null,
)

data class BannerUiModel(
    val showSuccess: List<Banner>?
)



