package com.wlq.willymodule.common.utils.livedata

import com.wlq.willymodule.base.util.LogUtils

sealed class SingleViewEvent {
    data class Toast(val message: String) : SingleViewEvent()
    data class Log(@LogUtils.TYPE val type: Int, val message: String) : SingleViewEvent()
    object ShowLoadingDialog : SingleViewEvent()
    object DismissLoadingDialog : SingleViewEvent()
}