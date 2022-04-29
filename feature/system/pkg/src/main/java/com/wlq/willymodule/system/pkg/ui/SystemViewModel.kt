package com.wlq.willymodule.system.pkg.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.mvi.asLiveData
import com.wlq.willymodule.base.mvi.setState
import com.wlq.willymodule.common.base.BaseViewModel
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.system.pkg.data.rep.CollectRepository
import com.wlq.willymodule.system.pkg.data.rep.KnowledgeRepository
import com.wlq.willymodule.system.pkg.data.rep.NewProjectRepository
import com.wlq.willymodule.system.pkg.data.rep.SquareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SystemViewModel : BaseViewModel() {

    private val squareRep by lazy { SquareRepository() }
    private val projectRep by lazy { NewProjectRepository() }
    private val knowledgeRep by lazy { KnowledgeRepository() }
    private val collectRep by lazy { CollectRepository() }

    //列表UI状态
    private val _viewListUiStates: MutableLiveData<ArticleUiState> =
        MutableLiveData(ArticleUiState())
    val listUiStates = _viewListUiStates.asLiveData()

    sealed class ArticleType {
        object Square : ArticleType()   //广场
        object NewProject : ArticleType()   //最新项目
        object Knowledge : ArticleType()    //体系列表
    }

    private var currentPage = 0

    fun getSquareArticleList(isRefresh: Boolean = false) =
        getArticleList(ArticleType.Square, isRefresh)

    fun getNewProjectList(isRefresh: Boolean = false) =
        getArticleList(ArticleType.NewProject, isRefresh)

    fun getKnowledgeArticleList(isRefresh: Boolean = false, cid: Int) =
        getArticleList(ArticleType.Knowledge, isRefresh, cid)

    private fun getArticleList(articleType: ArticleType, isRefresh: Boolean = false, cid: Int = 0) {
        viewModelScope.launch(Dispatchers.Main) {
            setListUiStates(ListStatus.Loading)
            if (isRefresh) currentPage = 0
            val result = when (articleType) {
                ArticleType.Square -> squareRep.getArticleList(currentPage)
                ArticleType.NewProject -> projectRep.getNewProjectList(currentPage)
                ArticleType.Knowledge -> knowledgeRep.getSystemTypeDetail(cid, currentPage)
            }
            if (result is HttpResult.Success) {
                val data = result.data
                if (data.offset >= data.total) {
                    setListUiStates(ListStatus.LoadMoreEnd)
                    viewShowToastEvent("暂无更多数据")
                    return@launch
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
                    "getArticleList error:${(result as HttpResult.Error).apiException.errorMsg}"
                )
            }
        }
    }

    fun collectArticle(articleId: Int, isCollect: Boolean) {
        viewModelScope.launch {
            val result =
                if (isCollect) collectRep.collectArticle(articleId) else collectRep.unCollectArticle(
                    articleId
                )
            if (result is HttpResult.Success) {

            } else if (result is HttpResult.Error) {
                viewShowToastEvent(message = result.apiException.errorMsg)
            }
        }
    }

    private fun setListUiStates(listStatus: ListStatus) {
        _viewListUiStates.setState {
            copy(listStatus = listStatus)
        }
    }
}