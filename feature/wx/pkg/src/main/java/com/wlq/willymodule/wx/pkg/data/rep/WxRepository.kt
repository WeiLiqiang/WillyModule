package com.wlq.willymodule.wx.pkg.data.rep

import com.wlq.willymodule.common.base.BaseRepository
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.wx.pkg.data.api.WxRetrofitClient
import com.wlq.willymodule.wx.pkg.data.bean.SystemParent

class WxRepository : BaseRepository() {

    private val apiService by lazy { WxRetrofitClient.service }

    suspend fun getWxBlog(): HttpResult<List<SystemParent>> {
        return safeApiCall(call = {requestWxBlogList()}, specifiedMessage = "")
    }
    private suspend fun requestWxBlogList() =
        executeResponse(apiService.getBlogType())
}