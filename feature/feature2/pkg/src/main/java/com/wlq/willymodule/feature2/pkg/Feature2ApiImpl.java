package com.wlq.willymodule.feature2.pkg;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.base.util.ApiUtils;
import com.wlq.willymodule.feature2.export.api.Feature2Api;
import com.wlq.willymodule.feature2.export.model.Feature2Param;
import com.wlq.willymodule.feature2.export.model.Feature2Result;
import com.wlq.willymodule.feature2.pkg.ui.Feature2Activity;

@ApiUtils.Api
public class Feature2ApiImpl extends Feature2Api {

    @Override
    public Feature2Result startFeature1Activity(Context context, Feature2Param param) {
        Feature2Activity.start(context);
        LogUtils.i(param.getName());
        return new Feature2Result("I am Feature2Result");
    }

    @Override
    public void startFeature2Activity(Context context) {
        Feature2Activity.start(context);
    }
}
