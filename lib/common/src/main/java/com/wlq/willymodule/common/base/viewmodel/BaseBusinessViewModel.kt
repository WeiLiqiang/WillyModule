package com.wlq.willymodule.common.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.mvi.intent.SingleEvent
import com.wlq.willymodule.base.mvi.intent.UiMultipleEvent
import com.wlq.willymodule.base.mvi.intent.UiState
import com.wlq.willymodule.base.mvi.vm.BaseViewModel
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.http.model.HttpResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit
typealias Cancel = suspend (Exception) -> Unit

abstract class BaseBusinessViewModel<State : UiState, Event : UiMultipleEvent> : BaseViewModel() {

    /**
     * 初始状态
     * stateFlow区别于LiveData必须有初始值
     */
    private val initialState: State by lazy { createInitialState() }

    abstract fun createInitialState(): State

    /**
     * uiState聚合页面的全部UI 状态
     */
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    /**
     * event包含用户与ui的交互（如点击操作），也有来自后台的消息
     */
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    protected fun setState(reduce: State.() -> State) {
        val newState = initialState.reduce()
        _uiState.value = newState
    }

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    protected abstract fun handleEvent(event: Event)

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param cancel 取消时执行
     * @return Job
     */
    fun launchMain(
        block: Block<Unit>,
        cancel: Cancel? = null
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        setSingleEvent {
                            SingleEvent.ShowLog(LogUtils.E, e.message.toString())
                        }
                        cancel?.invoke(e)
                    }
                }
            }
        }
    }

    protected fun <T : Any> httpResultSuccess(httpResult: HttpResult<T>): Boolean {
        return when (httpResult) {
            is HttpResult.Success -> true
            else -> false
        }
    }
}