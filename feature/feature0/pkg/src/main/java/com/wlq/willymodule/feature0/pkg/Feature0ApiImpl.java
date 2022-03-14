package com.wlq.willymodule.feature0.pkg;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.base.util.ApiUtils;
import com.wlq.willymodule.feature0.export.Feature0Param;
import com.wlq.willymodule.feature0.export.Feature0Result;
import com.wlq.willymodule.feature0.export.api.Feature0Api;
import com.wlq.willymodule.feature0.pkg.ui.Feature0Activity;

@ApiUtils.Api
public class Feature0ApiImpl extends Feature0Api {

    @Override
    public Feature0Result startFeature0Activity(Context context, Feature0Param param) {
        Feature0Activity.start(context);
        LogUtils.i(param.getName());
        return new Feature0Result("I am Feature0Result");
    }
}
