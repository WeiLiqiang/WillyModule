package com.wlq.willymodule.main.pkg.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.business.network.cookie.store.MemoryCookieStore
import com.wlq.willymodule.base.business.network.cookie.store.PersistentCookieStore
import com.wlq.willymodule.base.mvi.asLiveData
import com.wlq.willymodule.base.mvi.asLiveDataDiff
import com.wlq.willymodule.base.mvi.setState
import com.wlq.willymodule.base.util.ToastUtils
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.model.bean.UserInfo
import com.wlq.willymodule.common.model.store.UserInfoStore
import com.wlq.willymodule.main.pkg.data.rep.MainRepository
import com.wlq.willymodule.main.pkg.ui.action.MainViewLoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel : BaseBusinessViewModel() {

    private val repository by lazy { MainRepository() }
    private var userInfo: UserInfo? = null

    init {
        userInfo = UserInfoStore.getUserInfo()
    }

    private val _viewLoginStates = MutableLiveData(MainViewLoginState())
    val viewLoginState = _viewLoginStates.asLiveData()

    fun updateUserInfo(userInfo: UserInfo) {
        _viewLoginStates.setState { copy(username = userInfo.nickname) }
        _viewLoginStates.setState { copy(headerUrl = userInfo.icon) }
        _viewLoginStates.setState { copy(loginSuccess = true) }
    }

    fun isLogin(): Boolean {
        return userInfo != null
    }

    fun getUserInfo(): UserInfo? {
        return userInfo
    }

    fun logout() {
        launchMain(block = {
            val result = repository.logout()
            if (result is HttpResult.Success) {
                userInfo = null
                UserInfoStore.clearUserInfo()
                PersistentCookieStore().removeAllCookie()
                MemoryCookieStore().removeAllCookie()
                _viewLoginStates.setState { copy(username = "登录", loginSuccess = false) }
            }
        })
    }
}