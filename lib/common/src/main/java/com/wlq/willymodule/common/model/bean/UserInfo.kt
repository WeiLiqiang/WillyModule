package com.wlq.willymodule.common.model.bean

data class UserInfo(
    val admin: Boolean,
    val chapterTops: MutableList<Any>,
    val coinCount: Int,
    val collectIds: MutableList<Long>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)