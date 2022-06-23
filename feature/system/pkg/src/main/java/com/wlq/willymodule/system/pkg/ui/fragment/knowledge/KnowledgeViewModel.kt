package com.wlq.willymodule.system.pkg.ui.fragment.knowledge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.mvi.asLiveDataDiff
import com.wlq.willymodule.base.mvi.setState
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.base.mvi.viewmodel.BaseViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.system.pkg.data.bean.SystemParent
import com.wlq.willymodule.system.pkg.data.rep.KnowledgeBusinessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KnowledgeViewModel : BaseViewModel() {

    private val repository by lazy { KnowledgeBusinessRepository() }

    private val _listUiStates: MutableLiveData<KnowledgeUiState> =
        MutableLiveData(KnowledgeUiState())
    val listUiStates = _listUiStates.asLiveDataDiff()

    fun getSystemTypes(isRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            setListUiStates(ListStatus.Loading)
            val result = withContext(Dispatchers.IO) {
                repository.getSystemTypes()
            }
            if (result is HttpResult.Success) {
                _listUiStates.setState {
                    copy(
                        listStatus = ListStatus.Success,
                        dataList = Pair(if (isRefresh) IsRefresh.TRUE else IsRefresh.FALSE, result.data)
                    )
                }
            } else {
                setListUiStates(ListStatus.Error)
                viewShowLogEvent(
                    LogUtils.E,
                    "getSystemTypes error:${(result as HttpResult.Error).apiException.errorMsg}"
                )
            }
        }
    }

    private fun setListUiStates(listStatus: ListStatus) {
        _listUiStates.setState {
            copy(listStatus = listStatus)
        }
    }
}

data class KnowledgeUiState(
    val listStatus: ListStatus = ListStatus.Loading,
    val dataList: Pair<IsRefresh, List<SystemParent>?>? = null,
)
