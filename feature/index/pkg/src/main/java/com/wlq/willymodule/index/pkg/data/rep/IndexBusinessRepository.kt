package com.wlq.willymodule.index.pkg.data.rep

import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseBusinessRepository
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.HttpError
import com.wlq.willymodule.index.pkg.data.api.IndexRetrofitClient
import com.wlq.willymodule.index.pkg.data.bean.Banner
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.index.pkg.data.bean.IndexArticle

class IndexBusinessRepository : BaseBusinessRepository() {

    private val httpService by lazy { IndexRetrofitClient.service }

    suspend fun getBanners(): HttpResult<List<Banner>> {
        return safeApiCall(
            call = { requestBanners() },
            handleCustomError = { handleError(it) }
        )
    }

    private fun handleError(httpError: HttpError) {
        if (httpError.code == HttpError.UNKNOWN_HOST_ERROR.code) {
            LogUtils.i("handleError", "${Thread.currentThread().name}:正在处理首页请求错误异常逻辑...")
        }
    }

    private suspend fun requestBanners(): HttpResult<List<Banner>> =
        executeResponse(httpService.getBanner())

    suspend fun getArticleList(currentPage: Int): HttpResult<ApiPageResponse<IndexArticle>> {
        return safeApiCall(call = { requestArticleList(currentPage) }, specifiedMessage = "")
    }

    private suspend fun requestArticleList(page: Int): HttpResult<ApiPageResponse<IndexArticle>> =
        executeResponse(httpService.getHomeArticles(page))

    suspend fun collectArticle(articleId: Int): HttpResult<ApiPageResponse<IndexArticle>> {
        return safeApiCall(call = { requestCollectArticle(articleId) }, specifiedMessage = "")
    }

    private suspend fun requestCollectArticle(articleId: Int): HttpResult<ApiPageResponse<IndexArticle>> =
        executeResponse(httpService.collectArticle(articleId))

    suspend fun unCollectArticle(articleId: Int): HttpResult<ApiPageResponse<IndexArticle>> {
        return safeApiCall(call = { requestUnCollectArticle(articleId) }, specifiedMessage = "")
    }

    private suspend fun requestUnCollectArticle(articleId: Int): HttpResult<ApiPageResponse<IndexArticle>> =
        executeResponse(httpService.collectArticle(articleId))
}