package com.wlq.willymodule.project.pkg.data.api

import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.ApiResponse
import com.wlq.willymodule.common.http.retrofit.CommonRetrofitClient
import com.wlq.willymodule.project.pkg.data.bean.ProjectDetail
import com.wlq.willymodule.project.pkg.data.bean.SystemParent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApiService {

    @GET("/project/tree/json")
    suspend fun getProjectTypeList(): ApiResponse<List<SystemParent>>

    @GET("/project/list/{page}/json")
    suspend fun getProjectTypeDetailList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPageResponse<ProjectDetail>>
}

object ProjectRetrofitClient : CommonRetrofitClient() {

    val apiService by lazy { getService(ProjectApiService::class.java) }
}