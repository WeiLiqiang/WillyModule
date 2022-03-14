package com.wlq.willymodule.feature0.export.api;

import android.content.Context;

import com.wlq.willymodule.base.util.ApiUtils;
import com.wlq.willymodule.feature0.export.Feature0Param;
import com.wlq.willymodule.feature0.export.Feature0Result;

public abstract class Feature0Api extends ApiUtils.BaseApi {

    public abstract Feature0Result startFeature0Activity(Context context, Feature0Param param);
}
