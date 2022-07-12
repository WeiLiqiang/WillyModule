package com.wlq.willymodule.wx.pkg.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wlq.willymodule.base.mvi.asLiveDataDiff
import com.wlq.willymodule.base.mvi.setState
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.wx.pkg.data.rep.WxRepository
import com.wlq.willymodule.wx.pkg.ui.action.ListUiStates
import com.wlq.willymodule.wx.pkg.ui.action.WxIntent

class WxChildViewModel : BaseBusinessViewModel() {

    private val repository by lazy { WxRepository() }
    private val _viewListUiStates: MutableLiveData<ListUiStates> =
        MutableLiveData(ListUiStates())
    val listUiStates = _viewListUiStates.asLiveDataDiff()

    private var currentPage = 0

    fun dispatchViewIntent(it: WxIntent) {
        when (it) {
            is WxIntent.RefreshListData -> refreshListData(it.isRefresh, it.cid)
            is WxIntent.CollectArticle -> collectArticle(it.collectId, it.collect)
            else -> {
            }
        }
    }

    private fun refreshListData(isRefresh: Boolean, cid: Int) {
        launchMain(block = {
            setListUiStates(ListStatus.Loading)
            if (isRefresh) currentPage = 0
            val result = repository.getWxChildList(currentPage, cid)
            if (result is HttpResult.Success) {
                val data = result.data
                if (data.offset >= data.total) {
                    setListUiStates(ListStatus.LoadMoreEnd)
                    viewShowToastEvent("暂无更多数据")
                    return@launchMain
                }
                currentPage++
                _viewListUiStates.setState {
                    copy(
                        listStatus = ListStatus.Success,
                        articleList = Pair(if (isRefresh) IsRefresh.TRUE else IsRefresh.FALSE, data)
                    )
                }
            } else {
                setListUiStates(ListStatus.Error)
                viewShowLogEvent(
                    LogUtils.E,
                    "refreshListData error:${(result as HttpResult.Error).apiException.errorMsg}"
                )
            }
        })
    }

    private fun collectArticle(articleId: Int, isCollect: Boolean) {
//        viewModelScope.launch {
//            val result =
//                if (isCollect) collectRep.collectArticle(articleId) else collectRep.unCollectArticle(
//                    articleId
//                )
//            if (result is HttpResult.Success) {
//
//            } else if (result is HttpResult.Error) {
//                viewShowToastEvent(message = result.apiException.errorMsg)
//            }
//        }
    }

    private fun setListUiStates(listStatus: ListStatus) {
        _viewListUiStates.setState {
            copy(listStatus = listStatus)
        }
    }
}