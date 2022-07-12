package com.wlq.willymodule.main.pkg.data.rep

import com.wlq.willymodule.common.base.BaseBusinessRepository
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.main.pkg.data.api.LoginRetrofitClient

class MainRepository : BaseBusinessRepository() {

    private val apiService by lazy { LoginRetrofitClient.service }

    suspend fun logout(): HttpResult<Any> {
        return safeApiCall(call = { executeResponse(apiService.logout()) })
    }
}