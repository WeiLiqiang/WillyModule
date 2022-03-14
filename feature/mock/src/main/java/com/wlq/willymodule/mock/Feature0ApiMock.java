package com.wlq.willymodule.mock;

import android.content.Context;

import com.blankj.utilcode.util.ApiUtils;
import com.wlq.willymodule.feature0.export.Feature0Param;
import com.wlq.willymodule.feature0.export.Feature0Result;
import com.wlq.willymodule.feature0.export.api.Feature0Api;

@ApiUtils.Api(isMock = true)
public class Feature0ApiMock extends Feature0Api {

    @Override
    public Feature0Result startFeature0Activity(Context context, Feature0Param param) {
        return new Feature0Result("I am Feature0ApiMock");
    }
}
