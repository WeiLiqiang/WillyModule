package com.wlq.willymodule.main.pkg.ui.viewmodel

import com.wlq.willymodule.base.business.network.cookie.store.MemoryCookieStore
import com.wlq.willymodule.base.business.network.cookie.store.PersistentCookieStore
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.model.bean.UserInfo
import com.wlq.willymodule.common.model.store.UserInfoStore
import com.wlq.willymodule.main.pkg.data.rep.MainRepository
import com.wlq.willymodule.main.pkg.ui.intent.MainEvent
import com.wlq.willymodule.main.pkg.ui.intent.MainLoginStatus
import com.wlq.willymodule.main.pkg.ui.intent.MainState

class MainViewModel : BaseBusinessViewModel<MainState, MainEvent>() {

    private val repository by lazy { MainRepository() }
    private var userInfo: UserInfo? = null

    init {
        userInfo = UserInfoStore.getUserInfo()
    }

    override fun createInitialState(): MainState {
        return MainState()
    }

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.UpdateUserInfo -> {
                updateUserInfo(event.userInfo)
            }
        }
    }

    private fun updateUserInfo(userInfo: UserInfo) {
        setState { copy(username = userInfo.nickname) }
        setState { copy(headerUrl = userInfo.icon) }
        setState { copy(loginStatus = MainLoginStatus.SUCCESS) }
    }

    fun isLogin(): Boolean {
        return userInfo != null
    }

    fun getUserInfo(): UserInfo? {
        return userInfo
    }

    fun logout() {
        launchMain({
            val result = repository.logout()
            if (result is HttpResult.Success) {
                userInfo = null
                UserInfoStore.clearUserInfo()
                PersistentCookieStore().removeAllCookie()
                MemoryCookieStore().removeAllCookie()
                setState { copy(username = "登录", loginStatus = MainLoginStatus.FAILURE("logout")) }
            }
        })
    }
}