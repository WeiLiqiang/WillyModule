package com.wlq.willymodule.main.pkg.ui.viewmodel

import com.wlq.willymodule.base.util.BusUtils
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.model.store.UserInfoStore
import com.wlq.willymodule.main.pkg.data.constant.MainConstants
import com.wlq.willymodule.main.pkg.data.rep.LoginRepository
import com.wlq.willymodule.main.pkg.ui.intent.LoginEnable
import com.wlq.willymodule.main.pkg.ui.intent.LoginEvent
import com.wlq.willymodule.main.pkg.ui.intent.LoginState
import com.wlq.willymodule.main.pkg.ui.intent.LoginStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class LoginViewModel : BaseBusinessViewModel<LoginState, LoginEvent>() {

    private val repository by lazy { LoginRepository() }

    override fun createInitialState(): LoginState {
        return LoginState()
    }

    override fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UpdateUserOrPass -> updateUserName(event.userName, event.password, event.isUser)
            is LoginEvent.Login -> login(event.userName, event.password)
            else -> {
                register()
            }
        }
    }

    private fun updateUserName(userName: String, password: String, isUser: Boolean) {
        setState {
            if (isUser) {
                copy(userName = userName)
            } else {
                copy(password = password)
            }
        }
        updateLoginEnable(userName, password)
    }

    private fun updateLoginEnable(userName: String, password: String) {
        val enable = userName.isNotEmpty() && password.length >= 8
        setState { copy(loginEnable = if (enable) LoginEnable.Enable else LoginEnable.Disable) }
    }

    private fun login(userName: String, password: String) {
        launchMain(block = {
            flow<HttpResult<String>> {
                loginLogic(userName, password)
            }.onStart {
                viewShowLoading()
            }.onCompletion {
                viewDismissLoading()
            }.collect()
        })
    }

    private suspend fun loginLogic(userName: String, password: String) {
        val result = repository.login(userName, password)
        if (httpResultSuccess(result)) {
            result as HttpResult.Success
            BusUtils.postSticky(MainConstants.BUS_TAG_USER_NAME, result.data)
            setState { copy(loginStatus = LoginStatus.SUCCESS) }
            UserInfoStore.setUserInfo(result.data)
        } else {
            result as HttpResult.Error
            val msg = result.apiException.errorMsg
            viewShowToast(msg)
            viewShowLogError(msg)
            setState { copy(loginStatus = LoginStatus.FAILURE(msg)) }
        }
    }

    private fun register() {
        launchMain(block = {
            flow<HttpResult<String>> {
                registerResponse()
            }.onStart {
                viewShowLoading()
            }.onCompletion {
                viewDismissLoading()
            }.collect()
        })
    }

    private suspend fun registerResponse() {
        val result = repository.loginAnonimous()
        if (httpResultSuccess(result)) {
            result as HttpResult.Success
            viewShowLog("registerResponse success:${result.data}")
        } else {
            result as HttpResult.Error
            viewShowLogError("registerResponse fail:${result}")
        }
    }
}