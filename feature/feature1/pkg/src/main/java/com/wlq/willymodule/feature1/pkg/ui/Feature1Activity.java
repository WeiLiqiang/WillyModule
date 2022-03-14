package com.wlq.willymodule.feature1.pkg.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.wlq.willymodule.base.BaseActivity;
import com.wlq.willymodule.base.util.BusUtils;
import com.wlq.willymodule.feature1.pkg.R;

public class Feature1Activity extends BaseActivity {

    private static final String TAG_ONE_PARAM = "TAG_ONE_PARAM";

    @BusUtils.Bus(tag = TAG_ONE_PARAM, sticky = true)
    public void oneParamFun(String param) {
        LogUtils.i(param);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Feature1Activity.class);
        context.startActivity(starter);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_feature1;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        BusUtils.register(this);
    }

    @Override
    public void doBusiness() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusUtils.unregister(this);
    }
}
