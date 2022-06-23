package com.wlq.willymodule.base.mvi.viewmodel

import androidx.lifecycle.ViewModel
import com.wlq.willymodule.base.mvi.asLiveData
import com.wlq.willymodule.base.mvi.asLiveDataDiff
import com.wlq.willymodule.base.mvi.livedata.LiveEvents
import com.wlq.willymodule.base.mvi.livedata.SingleViewEvent
import com.wlq.willymodule.base.mvi.setEvent
import com.wlq.willymodule.base.util.LogUtils

open class BaseViewModel : ViewModel() {

    //一些通用的一次性事件，封装到基类中
    private val _singleViewEvens: LiveEvents<SingleViewEvent> = LiveEvents()
    val singleViewEvens = _singleViewEvens.asLiveData()

    fun viewShowLogEvent(@LogUtils.TYPE type: Int, message: String) {
        _singleViewEvens.setEvent(
            SingleViewEvent.Log(type, message = message)
        )
    }

    fun viewShowToastEvent(message: String) {
        _singleViewEvens.setEvent(SingleViewEvent.Toast(message = message))
    }

    fun viewShowLoadingEvent() {
        _singleViewEvens.setEvent(
            SingleViewEvent.ShowLoadingDialog
        )
    }

    fun viewDismissLoadingEvent() {
        _singleViewEvens.setEvent(
            SingleViewEvent.DismissLoadingDialog
        )
    }

    fun viewDismissLoadingAndToast(toastMsg: String) {
        _singleViewEvens.setEvent(
            SingleViewEvent.DismissLoadingDialog, SingleViewEvent.Toast(message = toastMsg)
        )
    }
}