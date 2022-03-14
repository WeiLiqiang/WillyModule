package com.wlq.willymodule.mock

import android.content.Context
import com.blankj.utilcode.util.ApiUtils
import com.blankj.utilcode.util.LogUtils
import com.wlq.willymodule.feature1.export.api.Feature1Api
import com.wlq.willymodule.feature1.export.model.Feature1Param
import com.wlq.willymodule.feature1.export.model.Feature1Result

@ApiUtils.Api(isMock = true)
class Feature1ApiMock : Feature1Api() {

    override fun startFeature1Activity(context: Context?, param: Feature1Param?): Feature1Result {
        LogUtils.i(param?.name)
        return Feature1Result("I am Feature1ApiMock")
    }

    override fun startFeature1Activity(context: Context?) {

    }
}