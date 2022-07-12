package com.wlq.willymodule.common.task.utils

import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.task.FinalTaskRegister
import com.wlq.willymodule.common.task.core.TaskInfo
import com.wlq.willymodule.common.task.core.toTaskInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

internal class TaskManager private constructor(
    private val taskStart: TaskStart
) {

    private val completedTasks: MutableSet<String> = mutableSetOf()
    private lateinit var taskMap: Map<String, TaskInfo>
    private val mutex = Mutex()

    private fun start() {
        if (taskStart.isDebug) {
            val cost = measureTimeMillis {
                startTask()
            }
            LogUtils.i(TAG, "锚点任务 总完成 时间 : ${cost}ms  activity启动")
        } else {
            startTask()
        }
    }

    private fun startTask() {
        runBlocking {
            //获取task 并过滤不符合进程的数据
            val taskList =
                FinalTaskRegister().taskList.toTaskInfo(taskStart.app, taskStart.processName)
            //判断是否有重名
            CheckTask.checkDuplicateName(taskList)
            taskMap = taskList.map {
                it.name to it
            }.toMap()
            val singleSyncTasks: MutableSet<TaskInfo> = mutableSetOf()
            val singleAsyncTasks: MutableSet<TaskInfo> = mutableSetOf()
            val anchorTasks: MutableSet<TaskInfo> = mutableSetOf()
            taskList.forEach { task ->
                if (task.anchor) {
                    anchorTasks.add(task)
                }
                when {
                    task.depends.isNotEmpty() -> {
                        //判断是否循环依赖
                        CheckTask.checkCircularDependency(listOf(task.name), task.depends, taskMap)
                        task.depends.forEach {
                            val depend = taskMap[it]
                            checkNotNull(depend) {
                                "找不到任务 [${task.name}] 的依赖任务 [$it]"
                            }
                            depend.children.add(task)
                        }
                    }
                    task.background -> {
                        singleAsyncTasks.add(task)
                    }
                    else -> {
                        singleSyncTasks.add(task)
                    }
                }
            }
            //锚点任务依赖的任务都转化为锚点任务
            for(anchorTask in anchorTasks) {
                anchor(anchorTask)
            }

            //无依赖的异步任务
            singleAsyncTasks.sortedWith(AnchorComparator()).forEach { task ->
                launchTask(task)
            }

            //无依赖的同步任务
            singleSyncTasks.sortedWith(AnchorComparator()).forEach { task ->
                launchTask(task)
            }
        }
    }

    private suspend fun CoroutineScope.launchTask(task: TaskInfo) {
        val dispatcher = if (task.background) {
            //子线程任务用子线程
            Dispatchers.Default
        } else if (!task.background && ThreadUtils.isInMainThread()) {
            //主线程任务并且当前线程为主线程用
            Dispatchers.Unconfined
        } else {
            //主线程任务，当前线程不为主线程，切换到主线程
            Dispatchers.Main
        }
        if (task.anchor) {
            //锚点任务跟随主线程生命周期
            launch(dispatcher) {
                execute(task)
            }
        } else {
            //非锚点任务生命周期独立
            GlobalScope.launch(dispatcher) {
                execute(task)
            }
        }
    }

    private suspend fun CoroutineScope.execute(task: TaskInfo) {
        if (taskStart.isDebug) {
            LogUtils.d(TAG, "任务 [${task.name}] 开始 运行进程: [${taskStart.processName}] " +
                    "运行线程: [${Thread.currentThread().name}]")
            val cost = measureTimeMillis {
                kotlin.runCatching {
                    task.task.execute(taskStart.app)
                }.onFailure {
                    LogUtils.e(TAG,  "任务 [${task.name}] error $it")
                }
            }
            LogUtils.d(TAG, "任务 [${task.name}] 锚点 ${task.anchor} 完成 运行进程: [${taskStart.processName}] " +
                    "运行线程: [${Thread.currentThread().name}], 使用时间 : ${cost}ms"
            )
        } else {
            kotlin.runCatching {
                task.task.execute(taskStart.app)
            }.onFailure {
                LogUtils.e(TAG,  "任务 [${task.name}] error $it")
            }
        }
        afterExecute(task.name, task.children)
    }

    private suspend fun CoroutineScope.afterExecute(name: String, children: Set<TaskInfo>) {
        val allowTasks = mutex.withLock {
            completedTasks.add(name)
            children.filter { completedTasks.containsAll(it.depends) }
        }
        if (ThreadUtils.isInMainThread()) {
            // 如果是主线程，先将异步任务放入队列，再执行同步任务
            allowTasks.filter { it.background }.sortedWith(AnchorComparator()).forEach {
                launchTask(it)
            }
            allowTasks.filter { it.background.not() }.sortedWith(AnchorComparator()).forEach { execute(it) }
        } else {
            allowTasks.sortedWith(AnchorComparator()).forEach {
                launchTask(it)
            }
        }
    }

    private fun anchor(anchorTask: TaskInfo) {
        for(dependName in anchorTask.depends) {
            val depend = taskMap[dependName]
            if (depend != null) {
                depend.anchor = true
                anchor(depend)
            }
        }
    }

    companion object {

        const val TAG = "TaskManager"

        fun start(taskStart: TaskStart) {
            LogUtils.i(TAG, "start")
            TaskManager(taskStart).start()
        }
    }
}