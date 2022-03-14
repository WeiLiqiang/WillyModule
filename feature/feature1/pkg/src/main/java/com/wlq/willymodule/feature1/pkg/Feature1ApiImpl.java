package com.wlq.willymodule.feature1.pkg;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.base.util.ApiUtils;
import com.wlq.willymodule.feature1.export.api.Feature1Api;
import com.wlq.willymodule.feature1.export.model.Feature1Param;
import com.wlq.willymodule.feature1.export.model.Feature1Result;
import com.wlq.willymodule.feature1.pkg.ui.Feature1Activity;

@ApiUtils.Api
public class Feature1ApiImpl extends Feature1Api {

    @Override
    public Feature1Result startFeature1Activity(Context context, Feature1Param param) {
        Feature1Activity.start(context);
        LogUtils.i(param.getName());
        return new Feature1Result("I am Feature1Result");
    }

    @Override
    public void startFeature1Activity(Context context) {
        Feature1Activity.start(context);
    }
}
