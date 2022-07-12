package com.wlq.willymodule.project.pkg.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.viewmodel.BaseBusinessViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.project.pkg.data.ProjectRepository
import com.wlq.willymodule.project.pkg.data.bean.SystemParent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewModel : BaseBusinessViewModel() {

    private val repository by lazy { ProjectRepository() }

    private val _projectTypeList: MutableLiveData<List<SystemParent>> = MutableLiveData()
    val projectListLiveData: LiveData<List<SystemParent>>
        get() = _projectTypeList

    fun getProjectTypeList() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                repository.getProjectTypeList()
            }
            if (result is HttpResult.Success) {
                _projectTypeList.value = result.data
            } else {
                viewShowLogEvent(LogUtils.E, "getBolgTypeList error:${(result as HttpResult.Error).apiException.errorMsg}")
            }
        }
    }
}