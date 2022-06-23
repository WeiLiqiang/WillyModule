package com.wlq.willymodule.common.model.store

import com.wlq.willymodule.base.business.network.core.MoshiHelper
import com.wlq.willymodule.base.util.SPUtils
import com.wlq.willymodule.common.model.bean.UserInfo

object UserInfoStore {

    private const val SP_USER_INFO = "sp_user_info"
    private const val KEY_USER_INFO = "userInfo"

    fun getUserInfo(): UserInfo? {
        val userInfoStr = SPUtils.getInstance(SP_USER_INFO).getString(KEY_USER_INFO, "")
        return if (userInfoStr.isNotEmpty()) {
            MoshiHelper.fromJson<UserInfo>(userInfoStr)
        } else {
            null
        }
    }

    fun setUserInfo(userInfo: UserInfo) =
        SPUtils.getInstance(SP_USER_INFO).put(KEY_USER_INFO, MoshiHelper.toJson(userInfo))

    fun clearUserInfo() {
        SPUtils.getInstance(SP_USER_INFO).clear()
    }

    fun addCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId !in it.collectIds) {
                it.collectIds.add(collectId)
                setUserInfo(it)
            }
        }
    }

    fun removeCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId in it.collectIds) {
                it.collectIds.remove(collectId)
                setUserInfo(it)
            }
        }
    }
}