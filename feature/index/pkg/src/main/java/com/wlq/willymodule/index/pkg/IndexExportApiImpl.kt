package com.wlq.willymodule.index.pkg

import com.wlq.willymodule.base.util.ApiUtils
import com.wlq.willymodule.index.export.constants.IndexExportApi

@ApiUtils.Api
class IndexExportApiImpl : IndexExportApi() {

    override fun getComponentName(): String? {
        return "Index"
    }
}