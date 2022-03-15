package com.wlq.willymodule.feature0.pkg.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.wlq.willymodule.base.BaseActivity;
import com.wlq.willymodule.base.camera.CameraView;
import com.wlq.willymodule.base.camera.listener.CameraCloseListener;
import com.wlq.willymodule.base.camera.listener.CameraOpenListener;
import com.wlq.willymodule.base.http.ArcCallback;
import com.wlq.willymodule.base.http.ArcResponse;
import com.wlq.willymodule.base.util.ApiUtils;
import com.wlq.willymodule.base.util.BusUtils;
import com.wlq.willymodule.feature0.pkg.R;
import com.wlq.willymodule.feature0.pkg.http.ApiFactory;
import com.wlq.willymodule.feature0.pkg.http.ArcApiService;
import com.wlq.willymodule.feature1.export.api.Feature1Api;
import com.wlq.willymodule.feature1.export.model.Feature1Param;
import com.wlq.willymodule.feature1.export.model.Feature1Result;

public class Feature0Activity extends BaseActivity {

    private CameraView cameraView;

    public static void start(Context context) {
        Intent starter = new Intent(context, Feature0Activity.class);
        context.startActivity(starter);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_feature0;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        cameraView = findViewById(R.id.camera_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.openCamera(new CameraOpenListener() {
            @Override
            public void onCameraOpened(int cameraFace) {
                LogUtils.i("onCameraOpened:" + cameraFace);
            }

            @Override
            public void onCameraOpenError(@NonNull Throwable throwable) {
                LogUtils.e("onCameraOpened:" + throwable.getMessage());
            }
        });
        cameraView.setCameraPreviewListener((data, size, format) -> {

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.closeCamera(new CameraCloseListener() {
            @Override
            public void onCameraClosed(int cameraFace) {

            }
        });
    }

    @Override
    public void doBusiness() {
        findViewById(R.id.btn_go_to_feature1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusUtils.postSticky("TAG_ONE_PARAM", "我来自Feature0Activity的粘性事件");
                Feature1Result feature1Result = ApiUtils.getApi(Feature1Api.class)
                        .startFeature1Activity(Feature0Activity.this, new Feature1Param("From Feature0"));
                ToastUtils.showShort(feature1Result.getName());
//                ApiFactory.INSTANCE.create(ArcApiService.class).getUserInfo()
//                        .enqueue(new ArcCallback<JsonObject>() {
//                            @Override
//                            public void onSuccess(@NonNull ArcResponse<JsonObject> response) {
//                                LogUtils.i(GsonUtils.toJson(response));
//                            }
//
//                            @Override
//                            public void onFailed(@NonNull Throwable throwable) {
//                                LogUtils.e(throwable.getMessage());
//                            }
//                        });
            }
        });
    }
}
