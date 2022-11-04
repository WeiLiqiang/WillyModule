package com.wlq.willymodule.common.http.model

import com.squareup.moshi.Json
import java.io.Serializable

data class ApiResponse<T>(val errorCode: Int, val errorMsg: String, val data: T) : Serializable

data class ApiPageResponse<T>(
    @Json(name = "total") val total: Int,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "curPage") val curPage: Int,
    @Json(name = "offset") val offset: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "over") val over: Boolean,
    @Json(name = "datas") var datas: List<T>
) : Serializable

data class ApiException(val errorCode: Int, val errorMsg: String, val originMsg: String) : Serializable