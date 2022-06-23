package com.wlq.willymodule.wx.pkg.data.api

import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.ApiResponse
import com.wlq.willymodule.common.http.retrofit.CommonRetrofitClient
import com.wlq.willymodule.wx.pkg.data.bean.Article
import com.wlq.willymodule.wx.pkg.data.bean.SystemParent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WxApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/wxarticle/chapters/json")
    suspend fun getBlogType(): ApiResponse<List<SystemParent>>

    @GET("/article/list/{page}/json")
    suspend fun getBlogList(@Path("page") page: Int, @Query("cid") cid: Int): ApiResponse<ApiPageResponse<Article>>
}

object WxRetrofitClient : CommonRetrofitClient() {

    val service by lazy { getService(WxApiService::class.java) }
}