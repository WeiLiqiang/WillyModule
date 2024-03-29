package com.wlq.willymodule.common.aop.startup.dispatcher

import com.wlq.willymodule.common.aop.startup.StartupTask
import com.wlq.willymodule.common.aop.startup.executor.ExecutorFactory
import com.wlq.willymodule.common.aop.startup.sort.StartupSortResult
import com.wlq.willymodule.common.aop.startup.utils.log
import com.wlq.willymodule.common.aop.startup.utils.trace
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis

internal class StartupDispatcher(sortResult: StartupSortResult) {

    companion object {
        private const val TAG = "StartupDispatcher"
    }

    // 任务集合
    private val startupList: List<StartupTask> = sortResult.startupList

    // 任务key: 任务
    private val startupMap: Map<String, StartupTask> = sortResult.startupMap

    // 任务key: 子任务key集合
    private val startupChildrenMap: Map<String, List<String>> = sortResult.startupChildrenMap

    // 用来阻塞总线程（App启动线程）
    private var countDownLatch: CountDownLatch? = null

    // 用来阻塞单个任务，等待依赖任务完成
    private val countDownLatchMap = ConcurrentHashMap<String, CountDownLatch>()

    // 执行器缓存
    private val executorFactoryCache = hashMapOf<Class<out ExecutorFactory>, ExecutorFactory>()

    fun dispatch(timeout: Long) {
        trace("dispatch") {
            val cost = measureTimeMillis {
                startupList.run {
                    // 计算异步且阻塞的任务
                    val size = filter { it.isBlock }.size
                    if (size > 0) {
                        countDownLatch = CountDownLatch(size)
                    }
                    // 已经排好序了，依次执行
                    forEach {
                        dispatch(it)
                    }
                    // 阻塞总线程（App启动线程）
                    countDownLatch?.await(timeout, TimeUnit.MILLISECONDS)
                }
            }
            log { "$TAG 所有任务耗时${cost}ms" }
        }
    }

    private fun dispatch(startup: StartupTask) {
        log { "$TAG ${startup.id}任务开始分发" }
        startup.executorFactory.getInstance().executor().execute {
            var start = System.currentTimeMillis()
            // 阻塞，等待父任务完成
            startup.myCountDownLatch().await()
            startup.awaitTime = System.currentTimeMillis() - start

            trace(startup.id) {
                log { "$TAG ${startup.id}任务执行开始" }
                start = System.currentTimeMillis()
                startup.startup()
                startup.startupTime = System.currentTimeMillis() - start
                log { "$TAG ${startup.id}任务执行完成" }
            }

            onStartupCompleted(startup)
        }
    }

    @Synchronized
    private fun onStartupCompleted(startup: StartupTask) {
        log { "$TAG ${startup.id} 任务已等待${startup.awaitTime}ms， 执行任务耗时${startup.startupTime}ms" }
        if (startup.isBlock) {
            // 释放总线程（App启动线程）
            countDownLatch?.countDown()
        }

        // 告诉子任务：我好了
        val uniqueKey = startup.id
        startupChildrenMap[uniqueKey]?.mapNotNull { childUniqueKey ->
            startupMap[childUniqueKey]
        }?.forEach { childStartup ->
            childStartup.myCountDownLatch().countDown()
        }
    }

    // 单例
    private fun KClass<out ExecutorFactory>.getInstance(): ExecutorFactory {
        return executorFactoryCache[java] ?: run {
            val newFactory = java.newInstance()
            executorFactoryCache[java] = newFactory
            newFactory
        }
    }

    // 获取指定任务的latch
    @Synchronized
    private fun StartupTask.myCountDownLatch(): CountDownLatch {
        val uniqueKey = this.id
        return countDownLatchMap[uniqueKey] ?: run {
            val newLatch = CountDownLatch(this.idDependencies.size)
            countDownLatchMap[uniqueKey] = newLatch
            newLatch
        }
    }

}