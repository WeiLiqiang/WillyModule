package com.wlq.willymodule.base.mvi.intent

import com.wlq.willymodule.base.util.LogUtils

interface UiSingleEvent

interface UiMultipleEvent

interface UiState

/**
 * 一次性消费事件，livedData版本，已废弃
 */
@Deprecated(
    "一次性消费事件，livedData版本，目前已废弃；" +
            "建议使用Flow、Channel版本替代"
)
sealed class CommonViewEvent {
    data class Toast(val message: String) : CommonViewEvent()
    data class Log(@LogUtils.TYPE val type: Int, val message: String) : CommonViewEvent()
    object ShowLoadingDialog : CommonViewEvent()
    object DismissLoadingDialog : CommonViewEvent()
}

/**
 * 一次性消费事件，Flow、Channel版本，主推版本
 */
sealed class SingleEvent : UiSingleEvent {

    data class ShowToast(val content: String) : SingleEvent()

    data class ShowLog(@LogUtils.TYPE val type: Int, val content: String) : SingleEvent()

    data class ShowCommonDialog(val title: String, val content: String) : SingleEvent()

    data class ShowLoadingDialog(val content: String) : SingleEvent()

    object DismissLoadingDialog : SingleEvent()
}

/**
 * 多次消费事件，Flow版本，主推版本
 */
sealed class MultipleEvent : UiMultipleEvent {

}