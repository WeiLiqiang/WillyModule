package com.wlq.willymodule.index.pkg.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.base.mvi.asLiveDataDiff
import com.wlq.willymodule.base.mvi.setState
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.index.pkg.data.bean.Banner
import com.wlq.willymodule.index.pkg.data.rep.IndexBusinessRepository
import kotlinx.coroutines.launch

class IndexViewModel : BaseBusinessViewModel() {

    private val repository by lazy { IndexBusinessRepository() }

    private val _uiBannerState = MutableLiveData<BannerUiModel>()
    val uiBannerStates: LiveData<BannerUiModel>
        get() = _uiBannerState

    //页面状态
    private val _collectViewStates: MutableLiveData<IndexCollectViewState> =
        MutableLiveData(IndexCollectViewState())
    val collectStates = _collectViewStates.asLiveDataDiff()
    private val _viewListStates: MutableLiveData<IndexListViewState> =
        MutableLiveData(IndexListViewState())
    val listStates = _viewListStates.asLiveDataDiff()

    private var currentPage = 0

    fun isLogin(): Boolean {
        return true
    }

    fun getBannerList() {
        viewModelScope.launch {
            val result = repository.getBanners()
            if (result is HttpResult.Success) {
                val bannerList = result.data
                emitBannerUiState(showSuccess = bannerList)
            } else {
                viewShowLogEvent(
                    LogUtils.E,
                    "getBannerList error:${(result as HttpResult.Error).apiException.errorMsg}"
                )
            }
        }
    }

    private fun emitBannerUiState(
        showSuccess: List<Banner>? = null
    ) {
        _uiBannerState.value = BannerUiModel(showSuccess)
    }

    data class BannerUiModel(
        val showSuccess: List<Banner>?
    )

    fun getArticleList(isRefresh: Boolean) {
        _viewListStates.setState {
            copy(listStatus = ListStatus.Loading)
        }
        if (isRefresh) currentPage = 0
        viewModelScope.launch {
            val result = repository.getArticleList(currentPage)
            if (result is HttpResult.Success) {
                val articleList = result.data
                if (articleList.offset >= articleList.total) {
                    _viewListStates.setState {
                        copy(listStatus = ListStatus.LoadMoreEnd)
                    }
                    viewShowToastEvent("暂无更多数据")
                    return@launch
                }
                currentPage++
                _viewListStates.setState {
                    copy(
                        listStatus = ListStatus.Success,
                        articleList = Pair(
                            if (isRefresh) IsRefresh.TRUE else IsRefresh.FALSE,
                            articleList
                        )
                    )
                }
            } else {
                _viewListStates.setState {
                    copy(listStatus = ListStatus.Error)
                }
                viewShowLogEvent(
                    LogUtils.E,
                    "getArticleList error:${(result as HttpResult.Error).apiException.errorMsg}"
                )
            }
        }
    }

    fun collectArticle(articleId: Int, isCollect: Boolean) {
        viewModelScope.launch {
            val result =
                if (isCollect) repository.collectArticle(articleId) else repository.unCollectArticle(
                    articleId
                )
            if (result is HttpResult.Success) {
                _collectViewStates.setState {
                    copy(collectArticleList = result.data)
                }
            } else if (result is HttpResult.Error) {
                viewShowToastEvent(message = result.apiException.errorMsg)
            }
        }
    }
}