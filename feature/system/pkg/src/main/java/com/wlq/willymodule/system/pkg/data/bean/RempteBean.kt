package com.wlq.willymodule.system.pkg.data.bean

import java.io.Serializable

//文章
data class Article(
    val id: Int,
    val originId: Int,
    val title: String,
    val chapterId: Int,
    val chapterName: String,
    val envelopePic: String,
    val link: String,
    val author: String,
    val origin: String,
    val publishTime: Long,
    val zan: Int,
    val desc: String,
    val visible: Int,
    val niceDate: String,
    val niceShareDate: String,
    val courseId: Int,
    var collect: Boolean,
    val apkLink: String,
    val projectLink: String,
    val superChapterId: Int,
    val superChapterName: String?,
    val type: Int,
    val fresh: Boolean,
    val audit: Int,
    val prefix: String,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val tags: Any, // Not sure
    val userId: Int
) : Serializable

data class SystemParent(val children: List<SystemChild>,
                        val courseId: Int,
                        val id: Int,
                        val name: String,
                        val order: Int,
                        val parentChapterId: Int,
                        val visible: Int,
                        val userControlSetTop: Boolean) : Serializable

data class SystemChild(val child: List<SystemChild>,
                       val courseId: Int,
                       val id: Int,
                       val name: String,
                       val order: Int,
                       val parentChapterId: Int,
                       val visible: Int):Serializable