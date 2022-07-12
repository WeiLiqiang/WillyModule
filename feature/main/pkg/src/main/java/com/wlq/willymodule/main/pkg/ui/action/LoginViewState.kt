package com.wlq.willymodule.main.pkg.ui.action

data class LoginViewState(
    val userName: String = "",
    val password: String = "",
    val loginSuccess: Boolean = false
) {

    val isLoginEnable: Boolean
        get() = userName.isNotEmpty() && password.length >= 6

    val passwordTipVisible: Boolean
        get() = password.length in 1..5
}