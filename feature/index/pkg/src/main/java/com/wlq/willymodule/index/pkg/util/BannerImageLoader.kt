package com.wlq.willymodule.index.pkg.util

import android.content.Context
import android.widget.ImageView
import com.wlq.willymodule.base.business.glide.GlideImageLoader
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        GlideImageLoader.getInstance().displayImage(context, path as String, imageView)
    }
}