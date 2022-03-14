package com.wlq.willymodule.feature1.export.api;

import android.content.Context;

import com.wlq.willymodule.base.util.ApiUtils;
import com.wlq.willymodule.feature1.export.model.Feature1Param;
import com.wlq.willymodule.feature1.export.model.Feature1Result;

public abstract class Feature1Api extends ApiUtils.BaseApi {

    public abstract Feature1Result startFeature1Activity(Context context, Feature1Param param);

    public abstract void startFeature1Activity(Context context);
}
