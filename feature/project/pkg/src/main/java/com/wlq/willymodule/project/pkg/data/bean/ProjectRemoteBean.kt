package com.wlq.willymodule.project.pkg.data.bean

import java.io.Serializable

data class ProjectDetail (
    val apkLink: String? = null,
    val audit: Int = 0,
    val author: String? = null,
    val canEdit: Boolean = false,
    val chapterId: Int = 0,
    val chapterName: String? = null,
    val collect: Boolean,
    val courseId: Int,
    val desc: String? = null,
    val descMd: String? = null,
    val envelopePic: String? = null,
    val fresh: Boolean? = null,
    val host: String? = null,
    val id: Int,
    val link: String? = null,
    val niceDate: String? = null,
    val niceShareDate: String? = null,
    val origin: String? = null,
    val prefix: String? = null,
    val projectLink: String? = null,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String? = null,
    val superChapterId: Int,
    val superChapterName: String? = null,
    val tags: List<Tag>? = null,
    val title: String? = null,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
) : Serializable

data class Tag(
    val name: String,
    val url: String
)

data class SystemParent(
    val children: List<SystemChild>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val visible: Int,
    val userControlSetTop: Boolean,
    val author: String,
    val cover: String,
    val desc: String,
    val lisense: String,
    val lisenseLink: String
) : Serializable

data class SystemChild(
    val child: List<SystemChild>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val visible: Int
) : Serializable