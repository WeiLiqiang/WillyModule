package com.wlq.willymodule.system.pkg.data.rep

import com.wlq.willymodule.common.base.BaseRepository
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.system.pkg.data.api.SystemRetrofitClient
import com.wlq.willymodule.system.pkg.data.bean.Article

class SquareRepository : BaseRepository() {

    private val apiService by lazy { SystemRetrofitClient.service }

    suspend fun getArticleList(currentPage: Int): HttpResult<ApiPageResponse<Article>> {
        return safeApiCall(call = { requestArticleList(currentPage) }, specifiedMessage = "")
    }

    private suspend fun requestArticleList(page: Int): HttpResult<ApiPageResponse<Article>> =
        executeResponse(apiService.getSquareArticleList(page))
}