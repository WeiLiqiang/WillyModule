package com.wlq.willymodule.main.pkg.data.rep

import com.wlq.willymodule.common.base.BaseBusinessRepository
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.model.bean.UserInfo
import com.wlq.willymodule.main.pkg.data.api.LoginRetrofitClient

class LoginRepository : BaseBusinessRepository() {

    private val httpService by lazy { LoginRetrofitClient.service }

    suspend fun login(userName: String, password: String): HttpResult<UserInfo> {
        return apiCall(
            call = { executeResponseHttpResult(httpService.login(userName, password)) },
            specifiedMessage = ""
        )
    }

    suspend fun loginAnonimous(): HttpResult<Any> {
        return apiCall(call = { executeResponseString(httpService.loginAnonimous()) })
    }
}