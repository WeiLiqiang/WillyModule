package com.wlq.willymodule.base.mvi.livedata

import com.wlq.willymodule.base.util.LogUtils

sealed class CommonViewEvent {
    data class Toast(val message: String) : CommonViewEvent()
    data class Log(@LogUtils.TYPE val type: Int, val message: String) : CommonViewEvent()
    object ShowLoadingDialog : CommonViewEvent()
    object DismissLoadingDialog : CommonViewEvent()
}