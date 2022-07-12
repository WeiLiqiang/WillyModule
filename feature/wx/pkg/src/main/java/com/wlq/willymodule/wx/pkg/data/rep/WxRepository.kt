package com.wlq.willymodule.wx.pkg.data.rep

import com.wlq.willymodule.common.base.BaseBusinessRepository
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.wx.pkg.data.api.WxRetrofitClient
import com.wlq.willymodule.wx.pkg.data.bean.Article
import com.wlq.willymodule.wx.pkg.data.bean.SystemParent

class WxRepository : BaseBusinessRepository() {

    private val apiService by lazy { WxRetrofitClient.service }

    suspend fun getWxBlog(): HttpResult<List<SystemParent>> {
        return safeApiCall(call = {requestWxBlogList()}, specifiedMessage = "")
    }
    private suspend fun requestWxBlogList() =
        executeResponse(apiService.getBlogType())

    suspend fun getWxChildList(currentPage: Int, cid: Int): HttpResult<ApiPageResponse<Article>> {
        return safeApiCall(call = { requestWxChildList(currentPage, cid) })
    }
    private suspend fun requestWxChildList(currentPage: Int, cid: Int): HttpResult<ApiPageResponse<Article>> =
        executeResponse(apiService.getBlogList(currentPage, cid))
}