package com.wlq.willymodule.main.pkg.ui.intent

import com.wlq.willymodule.base.mvi.intent.UiState

data class LoginState(
    val userName: String = "",
    val password: String = "",
    val loginStatus: LoginStatus = LoginStatus.DEFAULT,
    val loginEnable: LoginEnable = LoginEnable.Disable
) : UiState

sealed class LoginStatus {

    object DEFAULT : LoginStatus()

    object SUCCESS : LoginStatus()

    data class FAILURE(val msg: String) : LoginStatus()
}

sealed class LoginEnable {

    object Enable : LoginEnable()

    object Disable : LoginEnable()
}