package com.wlq.willymodule.system.pkg.data.rep

import com.wlq.willymodule.common.base.BaseRepository
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.system.pkg.data.api.SystemRetrofitClient
import com.wlq.willymodule.system.pkg.data.bean.Article
import com.wlq.willymodule.system.pkg.data.bean.SystemParent

class KnowledgeRepository : BaseRepository() {

    private val apiService by lazy { SystemRetrofitClient.service }

    suspend fun getSystemTypeDetail(cid: Int, currentPage: Int): HttpResult<ApiPageResponse<Article>> {
        return safeApiCall(call = { requestSystemTypeDetail(cid, currentPage) }, specifiedMessage = "")
    }
    private suspend fun requestSystemTypeDetail(cid: Int, currentPage: Int): HttpResult<ApiPageResponse<Article>> =
        executeResponse(apiService.getSystemTypeDetail(currentPage, cid))


    suspend fun getBlogArticle(cid: Int, currentPage: Int): HttpResult<ApiPageResponse<Article>> {
        return safeApiCall(call = {requestBlogArticle(cid, currentPage)}, specifiedMessage = "")
    }
    private suspend fun requestBlogArticle(cid: Int, currentPage: Int): HttpResult<ApiPageResponse<Article>> =
        executeResponse(apiService.getSystemTypeDetail(currentPage, cid))

    suspend fun getSystemTypes(): HttpResult<List<SystemParent>> {
        return safeApiCall(call = {requestSystemType()}, specifiedMessage = "")
    }
    private suspend fun requestSystemType(): HttpResult<List<SystemParent>> =
        executeResponse(apiService.getSystemType())
}