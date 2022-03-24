package com.wlq.willymodule.feature2.export.api;

import android.content.Context;

import com.wlq.willymodule.base.util.ApiUtils;
import com.wlq.willymodule.feature2.export.model.Feature2Param;
import com.wlq.willymodule.feature2.export.model.Feature2Result;

public abstract class Feature2Api extends ApiUtils.BaseApi {

    public abstract Feature2Result startFeature1Activity(Context context, Feature2Param param);

    public abstract void startFeature2Activity(Context context);
}
