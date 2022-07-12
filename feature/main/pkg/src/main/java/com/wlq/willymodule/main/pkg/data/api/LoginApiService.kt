package com.wlq.willymodule.main.pkg.data.api

import com.wlq.willymodule.common.http.model.ApiResponse
import com.wlq.willymodule.common.http.retrofit.CommonRetrofitClient
import com.wlq.willymodule.common.model.bean.UserInfo
import okhttp3.OkHttpClient
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<UserInfo>

    @GET("/user/logout/json")
    suspend fun logout(): ApiResponse<Any>

    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ApiResponse<String>
}

object LoginRetrofitClient : CommonRetrofitClient() {

    override var addCacheInterceptor: Boolean = false

    val service by lazy { getService(LoginApiService::class.java) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        super.handleBuilder(builder)
        builder.addInterceptor(LoginSessionInterceptor())
    }
}