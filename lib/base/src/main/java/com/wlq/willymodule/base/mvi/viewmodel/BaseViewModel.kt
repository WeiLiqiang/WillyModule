package com.wlq.willymodule.base.mvi.viewmodel

import androidx.lifecycle.ViewModel
import com.wlq.willymodule.base.mvi.asLiveData
import com.wlq.willymodule.base.mvi.livedata.CommonViewEvent
import com.wlq.willymodule.base.mvi.livedata.LiveEvents
import com.wlq.willymodule.base.util.LogUtils

open class BaseViewModel : ViewModel() {

    //一些通用的一次性事件，封装到基类中
    private val _singleViewEvens: LiveEvents<CommonViewEvent> = LiveEvents()
    val singleViewEvens = _singleViewEvens.asLiveData()

    fun viewShowLogErrorEvent(message: String) {
        _singleViewEvens.value = listOf(CommonViewEvent.Toast(message = message))
    }

    fun viewShowLogEvent(message: String) {
        _singleViewEvens.value = listOf(CommonViewEvent.Log(LogUtils.I, message = message))
    }

    fun viewShowLogEvent(@LogUtils.TYPE type: Int, message: String) {
        _singleViewEvens.value = listOf(CommonViewEvent.Log(type, message = message))
    }

    fun viewShowToastEvent(message: String) {
        _singleViewEvens.value = listOf(CommonViewEvent.Toast(message = message))
    }

    fun viewShowLoadingEvent() {
        _singleViewEvens.value = listOf(CommonViewEvent.ShowLoadingDialog)
    }

    fun viewDismissLoadingEvent() {
        _singleViewEvens.value = listOf(CommonViewEvent.DismissLoadingDialog)
    }

    fun viewDismissLoadingAndToast(toastMsg: String) {
        _singleViewEvens.value = listOf(CommonViewEvent.DismissLoadingDialog, CommonViewEvent.Toast(message = toastMsg))
    }
}