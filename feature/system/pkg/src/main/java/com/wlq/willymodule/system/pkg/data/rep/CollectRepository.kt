package com.wlq.willymodule.system.pkg.data.rep

import com.wlq.willymodule.common.base.BaseRepository
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.system.pkg.data.api.SystemRetrofitClient
import com.wlq.willymodule.system.pkg.data.bean.Article

class CollectRepository : BaseRepository() {

    private val apiService by lazy { SystemRetrofitClient.service }

    suspend fun collectArticle(articleId: Int): HttpResult<ApiPageResponse<Article>> {
        return safeApiCall(call = { requestCollectArticle(articleId) }, specifiedMessage = "")
    }
    private suspend fun requestCollectArticle(articleId: Int): HttpResult<ApiPageResponse<Article>> =
        executeResponse(apiService.collectArticle(articleId))

    suspend fun unCollectArticle(articleId: Int): HttpResult<ApiPageResponse<Article>> {
        return safeApiCall(call = { requestUnCollectArticle(articleId) }, specifiedMessage = "")
    }
    private suspend fun requestUnCollectArticle(articleId: Int): HttpResult<ApiPageResponse<Article>> =
        executeResponse(apiService.cancelCollectArticle(articleId))
}