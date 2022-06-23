package com.wlq.willymodule.index.pkg.data.api

import com.wlq.willymodule.common.http.model.ApiResponse
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.retrofit.CommonRetrofitClient
import com.wlq.willymodule.index.pkg.data.bean.Article
import com.wlq.willymodule.index.pkg.data.bean.Banner
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IndexApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/banner/json")
    suspend fun getBanner(): ApiResponse<List<Banner>>

    @GET("/article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): ApiResponse<ApiPageResponse<Article>>

    @POST("/lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): ApiResponse<ApiPageResponse<Article>>

    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(@Path("id") id: Int): ApiResponse<ApiPageResponse<Article>>
}

object IndexRetrofitClient : CommonRetrofitClient() {

    val service by lazy { getService(IndexApiService::class.java) }
}