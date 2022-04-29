package com.wlq.willymodule.system.pkg.ui

import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.system.pkg.data.bean.Article

data class ArticleUiState(
    val listStatus: ListStatus = ListStatus.Loading,
    val articleList: Pair<IsRefresh, ApiPageResponse<Article>?>? = null,
)