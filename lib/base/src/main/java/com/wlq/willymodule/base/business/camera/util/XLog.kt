package com.wlq.willymodule.base.business.camera.util

import java.util.concurrent.CopyOnWriteArrayList

object XLog {

    private var isDebug = true
    private var watchers: CopyOnWriteArrayList<LogWatcher> = CopyOnWriteArrayList()

    fun v(tag: String, msg: String?) {
        watchers.forEach { it.onLog(LogLevel.V, tag, msg) }
    }

    @JvmStatic
    fun d(tag: String, msg: String?) {
        watchers.forEach { it.onLog(LogLevel.D, tag, msg) }
    }

    @JvmStatic
    fun i(tag: String, msg: String?) {
        watchers.forEach { it.onLog(LogLevel.I, tag, msg) }
    }

    @JvmStatic
    fun w(tag: String, msg: String?) {
        watchers.forEach { it.onLog(LogLevel.W, tag, msg) }
    }

    @JvmStatic
    fun e(tag: String, msg: String?) {
        watchers.forEach { it.onLog(LogLevel.E, tag, msg) }
    }

    @JvmStatic
    fun setDebug(debug: Boolean) {
        isDebug = debug
    }

    @JvmStatic
    fun addLogWatcher(watcher: LogWatcher) {
        if (!watchers.contains(watcher)) {
            watchers.add(watcher)
        }
    }

    @JvmStatic
    fun removeLogWatcher(watcher: LogWatcher) {
        watchers.remove(watcher)
    }
}

interface LogWatcher {
    fun onLog(level: LogLevel, tag: String, msg: String?)
}

enum class LogLevel {
    V, D, I, W, E
}