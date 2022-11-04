package com.wlq.willymodule.main.pkg.ui.intent

import com.wlq.willymodule.base.mvi.intent.UiMultipleEvent

sealed class LoginEvent: UiMultipleEvent {

    /**
     * 更新账号信息
     */
    data class UpdateUserOrPass(val userName: String, val password: String, val isUser: Boolean): LoginEvent()

    /**
     * 登录
     */
    data class Login(val userName: String, val password: String): LoginEvent()

    /**
     * 注册
     */
    object Register: LoginEvent()
}