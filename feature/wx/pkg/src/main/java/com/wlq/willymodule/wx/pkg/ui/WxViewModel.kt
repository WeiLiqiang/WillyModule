package com.wlq.willymodule.wx.pkg.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlq.willymodule.base.util.LogUtils
import com.wlq.willymodule.common.base.BaseViewModel
import com.wlq.willymodule.common.http.model.HttpResult
import com.wlq.willymodule.wx.pkg.data.bean.SystemParent
import com.wlq.willymodule.wx.pkg.data.rep.WxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WxViewModel : BaseViewModel() {

    private val repository by lazy { WxRepository() }

    private val _blogTypeList: MutableLiveData<List<SystemParent>> = MutableLiveData()
    val blogListLiveData: LiveData<List<SystemParent>>
        get() = _blogTypeList

    fun getBolgTypeList() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                repository.getWxBlog()
            }
            if (result is HttpResult.Success) {
                _blogTypeList.value = result.data
            } else {
                viewShowLogEvent(LogUtils.E, "getBolgTypeList error:${(result as HttpResult.Error).apiException.errorMsg}")
            }
        }
    }
}