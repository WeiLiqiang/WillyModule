package com.wlq.willymodule.main.pkg.data.api

import com.wlq.willymodule.common.http.model.ApiResponse
import com.wlq.willymodule.common.http.retrofit.CommonRetrofitClient
import com.wlq.willymodule.common.model.bean.UserInfo
import com.wlq.willymodule.main.pkg.data.LoginSessionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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

    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ApiResponse<String>
}

object LoginRetrofitClient : CommonRetrofitClient() {

    val service by lazy { getService(LoginApiService::class.java) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        super.handleBuilder(builder)
        builder.addInterceptor(LoginSessionInterceptor())
    }
}