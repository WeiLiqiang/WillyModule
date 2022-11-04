package com.wlq.willymodule.main.pkg.ui.intent

import com.wlq.willymodule.base.mvi.intent.UiMultipleEvent
import com.wlq.willymodule.base.mvi.intent.UiState
import com.wlq.willymodule.common.model.bean.UserInfo

data class MainState(
    val username: String = "登录",
    val headerUrl: String = "",
    val loginStatus: MainLoginStatus = MainLoginStatus.FAILURE("")
) : UiState

sealed class MainLoginStatus {

    object SUCCESS : MainLoginStatus()

    data class FAILURE(val msg: String) : MainLoginStatus()
}

sealed class MainEvent: UiMultipleEvent {

    data class UpdateUserInfo(val userInfo: UserInfo) : MainEvent()
}