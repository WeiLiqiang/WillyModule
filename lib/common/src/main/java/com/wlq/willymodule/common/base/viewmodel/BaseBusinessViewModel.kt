package com.wlq.willymodule.common.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.mvi.viewmodel.BaseViewModel
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.http.model.HttpResult
import kotlinx.coroutines.*

typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit
typealias Cancel = suspend (Exception) -> Unit

open class BaseBusinessViewModel : BaseViewModel() {

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param cancel 取消时执行
     * @return Job
     */
    protected fun launchMain(
        block: Block<Unit>,
        cancel: Cancel? = null
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        viewShowLogEvent(LogUtils.E, e.message.toString())
                        cancel?.invoke(e)
                    }
                }
            }
        }
    }

    fun viewShowLogEvent(@LogUtils.TYPE type: Int, httpError: HttpResult<Any>) {
        val message = (httpError as HttpResult.Error).apiException.errorMsg
        viewShowLogEvent(type, message)
    }

    fun viewShowToastEvent(httpError: HttpResult<Any>) {
        val message = (httpError as HttpResult.Error).apiException.errorMsg
        viewShowToastEvent(message)
    }
}