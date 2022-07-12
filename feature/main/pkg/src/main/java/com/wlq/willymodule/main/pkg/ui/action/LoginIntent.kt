package com.wlq.willymodule.main.pkg.ui.action

import com.wlq.willymodule.base.mvi.IViewIntent

sealed class LoginIntent: IViewIntent {

    /**
     * 更新账号信息
     */
    data class UpdateUserName(val userName: String): LoginIntent()

    /**
     * 更新密码信息
     */
    data class UpdatePassword(val password: String): LoginIntent()

    /**
     * 登录
     */
    object Login: LoginIntent()

    /**
     * 注册
     */
    object Register: LoginIntent()
}