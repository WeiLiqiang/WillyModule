package com.wlq.willymodule.common.utils.livedata

sealed class IsRefresh {
    object TRUE : IsRefresh()
    object FALSE : IsRefresh()
}

sealed class ListStatus {
    object Loading : ListStatus()
    object Success : ListStatus()
    object Error : ListStatus()
    object LoadMoreEnd : ListStatus()
}