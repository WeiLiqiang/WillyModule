package com.wlq.willymodule.wx.pkg.data.api

import com.wlq.willymodule.base.business.network.BaseRetrofitClient
import com.wlq.willymodule.common.http.model.ApiResponse
import com.wlq.willymodule.wx.pkg.data.bean.SystemParent
import okhttp3.OkHttpClient
import retrofit2.http.GET

interface WxApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/wxarticle/chapters/json")
    suspend fun getBlogType(): ApiResponse<List<SystemParent>>
}

object WxRetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(WxApiService::class.java, WxApiService.BASE_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) = Unit
}