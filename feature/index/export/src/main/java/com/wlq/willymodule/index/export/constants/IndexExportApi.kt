package com.wlq.willymodule.index.export.constants

import com.wlq.willymodule.base.util.ApiUtils.BaseApi

abstract class IndexExportApi : BaseApi() {

    abstract fun getComponentName(): String?
}