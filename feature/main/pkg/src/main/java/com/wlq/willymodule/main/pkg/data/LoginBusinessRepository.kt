package com.wlq.willymodule.main.pkg.data

import com.wlq.willymodule.common.base.BaseBusinessRepository
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.main.pkg.data.api.LoginRetrofitClient
import com.wlq.willymodule.common.model.bean.UserInfo

class LoginBusinessRepository : BaseBusinessRepository() {

    private val httpService by lazy { LoginRetrofitClient.service }

    suspend fun login(userName: String, password: String): HttpResult<UserInfo> {
        return safeApiCall(call = { requestLogin(userName, password) })
    }

    private suspend fun requestLogin(
        userName: String,
        password: String
    ): HttpResult<UserInfo> =
        executeResponse(httpService.login(userName, password))
}