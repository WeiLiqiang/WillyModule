package com.wlq.willymodule.project.pkg.ui.action

import com.wlq.willymodule.common.http.model.ApiPageResponse
import com.wlq.willymodule.common.utils.livedata.IsRefresh
import com.wlq.willymodule.common.utils.livedata.ListStatus
import com.wlq.willymodule.project.pkg.data.bean.ProjectDetail

data class ListUiStates(
    val listStatus: ListStatus = ListStatus.Loading,
    val projectDetailList: Pair<IsRefresh, ApiPageResponse<ProjectDetail>?>? = null
)