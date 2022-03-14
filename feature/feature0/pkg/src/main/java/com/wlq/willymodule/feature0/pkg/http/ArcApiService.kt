package com.wlq.willymodule.feature0.pkg.http

import com.google.gson.JsonObject
import com.wlq.willymodule.base.http.ArcCall
import com.wlq.willymodule.base.http.annotation.GET

interface ArcApiService {

    @GET("user/lg/userinfo/json/")
    fun getUserInfo(): ArcCall<JsonObject>
}