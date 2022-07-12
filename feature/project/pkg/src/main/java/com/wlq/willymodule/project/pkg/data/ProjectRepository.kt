package com.wlq.willymodule.project.pkg.data

import com.wlq.willymodule.common.base.BaseBusinessRepository
import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.project.pkg.data.api.ProjectRetrofitClient
import com.wlq.willymodule.project.pkg.data.bean.ProjectDetail
import com.wlq.willymodule.project.pkg.data.bean.SystemParent

class ProjectRepository : BaseBusinessRepository() {

    private val apiService by lazy { ProjectRetrofitClient.apiService }

    suspend fun getProjectTypeList(): HttpResult<List<SystemParent>> =
        safeApiCall(call = { executeResponse(apiService.getProjectTypeList()) })


    suspend fun getProjectDetailList(page: Int, cid: Int): HttpResult<ApiPageResponse<ProjectDetail>> =
        safeApiCall(call = { executeResponse(apiService.getProjectTypeDetailList(page, cid)) })
}

