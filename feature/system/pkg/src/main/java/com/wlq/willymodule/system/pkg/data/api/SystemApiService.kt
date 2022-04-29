package com.wlq.willymodule.system.pkg.data.api

import com.wlq.willymodule.base.business.network.BaseRetrofitClient
import com.wlq.willymodule.common.http.model.ApiResponse
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.system.pkg.data.bean.Article
import com.wlq.willymodule.system.pkg.data.bean.SystemParent
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SystemApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/article/listproject/{page}/json")
    suspend fun getNewProjectList(@Path("page") page: Int): ApiResponse<ApiPageResponse<Article>>

    @GET("/user_article/list/{page}/json")
    suspend fun getSquareArticleList(@Path("page") page: Int): ApiResponse<ApiPageResponse<Article>>

    @GET("/tree/json")
    suspend fun getSystemType(): ApiResponse<List<SystemParent>>

    @GET("/article/list/{page}/json")
    suspend fun getSystemTypeDetail(@Path("page") page: Int, @Query("cid") cid: Int): ApiResponse<ApiPageResponse<Article>>

    @POST("/lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): ApiResponse<ApiPageResponse<Article>>

    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(@Path("id") id: Int): ApiResponse<ApiPageResponse<Article>>
}

object SystemRetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(SystemApiService::class.java, SystemApiService.BASE_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) = Unit
}