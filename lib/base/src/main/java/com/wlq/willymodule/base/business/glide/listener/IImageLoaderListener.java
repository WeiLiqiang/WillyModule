package com.wlq.willymodule.base.business.glide.listener;

import android.widget.ImageView;

public interface IImageLoaderListener {

    //监听图片下载错误
    void onLoadingFailed(String url, ImageView target, Exception exception);

    //监听图片加载成功
    void onLoadingComplete(String url, ImageView target);
}
