package com.wlq.willymodule.main.pkg.ui.viewmodel

import com.wlq.willymodule.base.mvi.setState
import com.wlq.willymodule.base.mvi.withState
import com.wlq.willymodule.base.util.BusUtils
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.model.bean.UserInfo
import com.wlq.willymodule.common.model.store.UserInfoStore
import com.wlq.willymodule.main.pkg.data.rep.LoginRepository
import com.wlq.willymodule.main.pkg.data.constant.MainConstants
import com.wlq.willymodule.main.pkg.ui.action.LoginIntent
import com.wlq.willymodule.main.pkg.ui.action.LoginViewState
import kotlinx.coroutines.flow.*

class LoginViewModel : BaseBusinessViewModel() {

    private val repository by lazy { LoginRepository() }
    private val _viewStates = MutableStateFlow(LoginViewState())
    val viewStates = _viewStates.asStateFlow()

    fun dispatchViewIntent(it: LoginIntent) {
        when (it) {
            is LoginIntent.UpdateUserName -> updateUserName(it.userName)
            is LoginIntent.UpdatePassword -> updatePassword(it.password)
            is LoginIntent.Login -> login()
            else -> {
                register()
            }
        }
    }

    private fun updateUserName(userName: String) {
        _viewStates.setState { copy(userName = userName) }
    }

    private fun updatePassword(password: String) {
        _viewStates.setState { copy(password = password) }
    }

    private fun login() {
        launchMain(block = {
            flow<HttpResult<String>> {
                loginLogic()
            }.onStart {
                viewShowLoadingEvent()
            }.onCompletion {
                viewDismissLoadingEvent()
            }.collect()
        })
    }

    private fun register() {

    }

    private suspend fun loginLogic() {
        withState(viewStates) {
            val userName = it.userName
            val password = it.password
            val result = repository.login(userName, password)
            if (result is HttpResult.Success) {
                loginSuccess(result)
            } else {
                viewShowToastEvent(result)
                viewShowLogEvent(LogUtils.E, result)
            }
        }
    }

    private fun loginSuccess(result: HttpResult.Success<UserInfo>) {
        BusUtils.postSticky(MainConstants.BUS_TAG_USER_NAME, result.data)
        _viewStates.setState { copy(loginSuccess = true) }
        UserInfoStore.setUserInfo(result.data!!)
    }
}