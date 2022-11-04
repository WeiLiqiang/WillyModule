package com.wlq.willymodule.base.mvi.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.mvi.intent.SingleEvent
import com.wlq.willymodule.base.util.LogUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    /**
     * 一些通用的一次性事件，且一对一的订阅关系
     * 1.例如：弹Toast、导航Fragment等
     * 2.使用flow和channel，解决liveData版本存在的问题
     */
    private val _singleEvent: Channel<SingleEvent> = Channel()
    val singleEvent = _singleEvent.receiveAsFlow()

    fun viewShowLogError(message: String) {
        setSingleEvent { SingleEvent.ShowLog(type = LogUtils.E, content = message) }
    }

    fun viewShowLog(message: String) {
        setSingleEvent { SingleEvent.ShowLog(type = LogUtils.I, content = message) }
    }

    fun viewShowLog(@LogUtils.TYPE type: Int, message: String) {
        setSingleEvent { SingleEvent.ShowLog(type = type, content = message) }
    }

    fun viewShowToast(message: String) {
        setSingleEvent { SingleEvent.ShowToast(content = message) }
    }

    fun viewShowLoading() {
        setSingleEvent { SingleEvent.ShowLoadingDialog("") }
    }

    fun viewDismissLoading() {
        setSingleEvent { SingleEvent.DismissLoadingDialog }
    }

    protected fun setSingleEvent(builder: () -> SingleEvent) {
        val newSingleEvent = builder()
        viewModelScope.launch {
            _singleEvent.send(newSingleEvent)
        }
    }
}