package com.wlq.willymodule.base.business.glide.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.wlq.willymodule.base.business.glide.listener.IGetBitmapListener;
import com.wlq.willymodule.base.business.glide.listener.IGetDrawableListener;
import com.wlq.willymodule.base.business.glide.listener.IImageLoaderListener;
import com.wlq.willymodule.base.business.glide.listener.ImageSize;
import com.wlq.willymodule.base.business.glide.okhttp.OnGlideImageViewListener;
import com.wlq.willymodule.base.business.glide.okhttp.OnProgressListener;
import com.wlq.willymodule.base.business.glide.okhttp.ProgressManager;
import com.wlq.willymodule.base.business.glide.transform.BlurBitmapTransformation;
import com.wlq.willymodule.base.business.glide.transform.GlideCircleTransformation;
import com.wlq.willymodule.base.business.glide.transform.RoundBitmapTransformation;
import com.wlq.willymodule.base.util.LogUtils;
import com.wlq.willymodule.base.util.ThreadUtils;

import java.io.File;

public class GlideImageLoaderClient implements IImageLoader {

    private static final String TAG = "GlideImageLoaderClient";
    private Handler mMainThreadHandler;
    private long mTotalBytes = 0;
    private long mLastBytesRead = 0;
    private boolean mLastStatus = false;
    private OnProgressListener internalProgressListener;
    private OnGlideImageViewListener onGlideImageViewListener;
    private OnProgressListener onProgressListener;

    @Override
    public void init(Context context) {

    }

    @Override
    public void destroy(Context context) {
        clearMemoryCache(context);
    }

    @Override
    public File getCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context);
    }

    @Override
    public void clearMemoryCache(Context context) {
        GlideApp.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        ThreadUtils.executeByCpu(new ThreadUtils.SimpleTask<Void>() {
            @Override
            public Void doInBackground() {
                GlideApp.get(context).clearDiskCache();
                return null;
            }

            @Override
            public void onSuccess(Void result) {

            }
        });
    }

    @Override
    public Bitmap getBitmapFromCache(Context context, String url) {
        throw new UnsupportedOperationException("glide 不支持同步 获取缓存中 bitmap");
    }

    @Override
    public void getBitmapFromCache(Context context, String url, IGetBitmapListener listener) {

        GlideApp.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (listener != null) {
                    listener.onBitmap(resource);
                }
            }
        });
    }

    @Override
    public void displayImage(Context context, int resId, ImageView imageView) {
        //设置缓存策略缓存原始数据  Saves just the original data to cache
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, boolean isCache) {
        GlideApp.with(context)
                .load(url)
                .skipMemoryCache(isCache)
                .diskCacheStrategy(isCache ? DiskCacheStrategy.AUTOMATIC : DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .placeholder(defRes).error(defRes).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .placeholder(defRes).error(defRes)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, BitmapTransformation transformations) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(circleRequestOptions(defRes, defRes)).into(imageView);
    }

    public void displayImageCircle(Fragment context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes, defRes)).into(imageView);
    }

    public RequestOptions requestOptions(int placeholderResId, int errorResId) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);
    }

    /**
     * 加载原图
     *
     * @param placeholderResId
     * @param errorResId
     * @return
     */
    public RequestOptions circleRequestOptions(int placeholderResId, int errorResId) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new GlideCircleTransformation());
    }

    public RequestOptions roundRequestOptions(int placeholderResId, int errorResId, int radius) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new RoundBitmapTransformation(radius));
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, BitmapTransformation transformations) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransformation(defRes, defRes, transformations)).into(imageView);
    }

    public RequestOptions requestOptionsTransformation(int placeholderResId, int errorResId, BitmapTransformation bitmapTransformation) {
        return requestOptions(placeholderResId, errorResId)
                .transform(bitmapTransformation);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, ImageSize size) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, ImageSize size) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, IImageLoaderListener listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                LogUtils.e(TAG, "Load failed", e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, IImageLoaderListener listener) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, IImageLoaderListener listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, IImageLoaderListener listener) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(circleRequestOptions(defRes, defRes)).into(imageView);
    }

    @Override
    public void displayCircleImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(circleRequestOptions(defRes, defRes)).into(imageView);
    }

    @Override
    public void displayRoundImage(Context context, String url, ImageView imageView, int defRes, int radius) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(roundRequestOptions(defRes, defRes, radius)).into(imageView);
    }

    @Override
    public void displayRoundImage(Fragment fragment, String url, ImageView imageView, int defRes, int radius) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(roundRequestOptions(defRes, defRes, radius)).into(imageView);
    }

    @Override
    public void displayBlurImage(Context context, String url, int blurRadius, IGetDrawableListener listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (listener != null) {
                    listener.onDrawable(resource);
                }
            }
        });
    }

    @Override
    public void displayBlurImage(Context context, String url, ImageView imageView, int defRes, int blurRadius) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(blurRequestOptions(defRes, defRes, blurRadius)).into(imageView);
    }

    private RequestOptions blurRequestOptions(int defRes, int defRes1, int blurRadius) {
        return requestOptions(defRes, defRes1)
                .transform(new BlurBitmapTransformation(blurRadius));
    }

    @Override
    public void displayBlurImage(Context context, int resId, ImageView imageView, int blurRadius) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(blurRequestOptions(resId, resId, blurRadius)).into(imageView);
    }

    @Override
    public void displayBlurImage(Fragment fragment, String url, ImageView imageView, int defRes, int blurRadius) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(blurRequestOptions(defRes, defRes, blurRadius)).into(imageView);
    }

    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView, BitmapTransformation transformations) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(transformations).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, BitmapTransformation transformations) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(transformations).into(imageView);
    }

    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView, int defRes) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).into(imageView);
    }

    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView, int defRes, BitmapTransformation transformations) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(transformations).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, int defRes, BitmapTransformation transformations) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(transformations).into(imageView);
    }

    @Override
    public void displayImageInResourceTransform(Activity activity, int resId, ImageView imageView, Transformation transformation, int errorResId) {
        GlideApp.with(activity).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).apply(requestOptionsTransform(errorResId, errorResId, transformation)).into(imageView);
    }

    @Override
    public void displayImageInResourceTransform(Context context, int resId, ImageView imageView, Transformation transformation, int errorResId) {

    }

    @Override
    public void displayImageInResourceTransform(Fragment fragment, int resId, ImageView imageView, Transformation transformation, int errorResId) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).apply(requestOptionsTransform(errorResId, errorResId, transformation)).into(imageView);
    }

    /**
     * 指定传入的那种图片的变形
     *
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @return
     */
    public RequestOptions requestOptionsTransform(int placeholderResId, int errorResId, Transformation transformation) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId).transform(transformation);
    }

    /**
     * 当传入到其他的东西的时候，我要保证图片不变形
     *
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @return
     */
    public RequestOptions requestOptionsNoTransform(int placeholderResId, int errorResId, Transformation<Bitmap> transformation) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId).transform(transformation);
    }

    @Override
    public void displayImageByNet(Context context, String url, ImageView imageView, int defRes, Transformation transformation) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransform(defRes, defRes, transformation)).into(imageView);
    }

    @Override
    public void displayImageByNet(Fragment fragment, String url, ImageView imageView, int defRes, Transformation transformation) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransform(defRes, defRes, transformation)).into(imageView);
    }

    @Override
    public void displayImageByNet(Activity activity, String url, ImageView imageView, int defRes, Transformation transformation) {
        GlideApp.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransform(defRes, defRes, transformation)).into(imageView);
    }

    @Override
    public void clear(Activity activity, ImageView imageView) {
        GlideApp.with(activity).clear(imageView);
    }

    @Override
    public void clear(Context context, ImageView imageView) {
        GlideApp.with(context).clear(imageView);
    }

    @Override
    public void clear(Fragment fragment, ImageView imageView) {
        GlideApp.with(fragment).clear(imageView);
    }

    //    默认的策略是DiskCacheStrategy.AUTOMATIC
    //    DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
    //    DiskCacheStrategy.NONE 不使用磁盘缓存
    //    DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
    //    DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
    //    DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
    @Override
    public void displayImageByDiskCacheStrategy(Fragment fragment, String url, DiskCacheStrategy diskCacheStrategy, ImageView imageView) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(diskCacheStrategy).into(imageView);
    }

    //    DiskCacheStrategy.NONE： 表示不缓存任何内容。
    //    DiskCacheStrategy.DATA： 表示只缓存原始图片。
    //    DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
    //    DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
    //    DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
    @Override
    public void displayImageByDiskCacheStrategy(Activity activity, String url, DiskCacheStrategy diskCacheStrategy, ImageView imageView) {
        GlideApp.with(activity).load(url).diskCacheStrategy(diskCacheStrategy).into(imageView);
    }

    @Override
    public void displayImageByDiskCacheStrategy(Context context, String url, DiskCacheStrategy diskCacheStrategy, ImageView imageView) {
        GlideApp.with(context).load(url).diskCacheStrategy(diskCacheStrategy).into(imageView);
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Fragment fragment, String url, ImageView imageView) {
        GlideApp.with(fragment)
                .load(url)
                .onlyRetrieveFromCache(true)
                .into(imageView);
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Activity activity, String url, ImageView imageView) {
        GlideApp.with(activity)
                .load(url)
                .onlyRetrieveFromCache(true)
                .into(imageView);
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .onlyRetrieveFromCache(true)
                .into(imageView);
    }

    /**
     * 如果你想确保一个特定的请求跳过磁盘和/或内存缓存（比如，图片验证码 –）
     *
     * @param fragment
     * @param url
     * @param imageView
     * @param skipflag         是否跳过内存缓存
     * @param diskCacheStratey 是否跳过磁盘缓存
     */
    @Override
    public void disPlayImageSkipMemoryCache(Fragment fragment, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        GlideApp.with(fragment)
                .load(url)
                .diskCacheStrategy(diskCacheStratey ? DiskCacheStrategy.NONE : DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(skipflag)
                .into(imageView);
    }

    @Override
    public void disPlayImageSkipMemoryCache(Activity activity, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        GlideApp.with(activity)
                .load(url)
                .diskCacheStrategy(diskCacheStratey ? DiskCacheStrategy.NONE : DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(skipflag)
                .into(imageView);
    }

    @Override
    public void disPlayImageSkipMemoryCache(Context context, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(diskCacheStratey ? DiskCacheStrategy.NONE : DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(skipflag)
                .into(imageView);
    }

    @Override
    public void disPlayImageErrorReload(Fragment fragment, String url, String fallbackUrl, ImageView imageView) {
        GlideApp.with(fragment)
                .load(url)
                .error(GlideApp.with(fragment)
                        .load(fallbackUrl))
                .into(imageView);
    }

    @Override
    public void disPlayImageErrorReload(Activity activity, String url, String fallbackUrl, ImageView imageView) {
        GlideApp.with(activity)
                .load(url)
                .error(GlideApp.with(activity)
                        .load(fallbackUrl))
                .into(imageView);
    }

    @Override
    public void disPlayImageErrorReload(Context context, String url, String fallbackUrl, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .error(GlideApp.with(context)
                        .load(fallbackUrl))
                .into(imageView);
    }

    @Override
    public void disPlayImageDisAllowHardwareConfig(Fragment fragment, String url, ImageView imageView) {
        GlideApp.with(fragment)
                .load(url)
                .disallowHardwareConfig()
                .into(imageView);
    }

    @Override
    public void disPlayImageDisAllowHardwareConfig(Activity activity, String url, ImageView imageView) {
        GlideApp.with(activity)
                .load(url)
                .disallowHardwareConfig()
                .into(imageView);
    }

    @Override
    public void disPlayImageDisAllowHardwareConfig(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .disallowHardwareConfig()
                .into(imageView);
    }

    @Override
    public void disPlayImageProgress(Context context, String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        onGlideImageViewListener = listener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl, bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    @Override
    public void disPlayImageProgress(Activity activity, String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {
        GlideApp.with(activity)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        onGlideImageViewListener = listener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl, bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    @Override
    public void disPlayImageProgress(Fragment fragment, String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {
        GlideApp.with(fragment)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        onGlideImageViewListener = listener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl, bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    @Override
    public void disPlayImageProgressByOnProgressListener(Context context, String url, ImageView imageView, int placeholderResId, int errorResId, OnProgressListener onProgressListener) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        this.onProgressListener = onProgressListener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl, bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    @Override
    public void disPlayImageProgressByOnProgressListener(Activity activity, String url, ImageView imageView, int placeholderResId, int errorResId, OnProgressListener onProgressListener) {
        GlideApp.with(activity)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        this.onProgressListener = onProgressListener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl, bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    @Override
    public void disPlayImageProgressByOnProgressListener(Fragment fragment, String url, ImageView imageView, int placeholderResId, int errorResId, OnProgressListener onProgressListener) {
        GlideApp.with(fragment)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url, mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        this.onProgressListener = onProgressListener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl, bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    @Override
    public void displayImageByTransition(Context context, String url, TransitionOptions transitionOptions, ImageView imageView) {
        if (transitionOptions instanceof DrawableTransitionOptions) {
            GlideApp.with(context)
                    .load(url)
                    .transition((DrawableTransitionOptions) transitionOptions)
                    .into(imageView);
        } else {
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .transition(transitionOptions)
                    .into(imageView);
        }
    }

    @Override
    public void displayImageByTransition(Activity activity, String url, TransitionOptions transitionOptions, ImageView imageView) {
        if (transitionOptions instanceof DrawableTransitionOptions) {
            GlideApp.with(activity)
                    .load(url)
                    .transition((DrawableTransitionOptions) transitionOptions)
                    .into(imageView);
        } else {
            GlideApp.with(activity)
                    .asBitmap()
                    .load(url)
                    .transition(transitionOptions)
                    .into(imageView);
        }
    }

    @Override
    public void displayImageByTransition(Fragment fragment, String url, TransitionOptions transitionOptions, ImageView imageView) {
        if (transitionOptions instanceof DrawableTransitionOptions) {
            GlideApp.with(fragment)
                    .load(url)
                    .transition((DrawableTransitionOptions) transitionOptions)
                    .into(imageView);
        } else {
            GlideApp.with(fragment)
                    .asBitmap()
                    .load(url)
                    .transition(transitionOptions)
                    .into(imageView);
        }
    }

    @Override
    public void glidePauseRequests(Context context) {
        GlideApp.with(context).pauseRequests();
    }

    @Override
    public void glidePauseRequests(Activity activity) {
        GlideApp.with(activity).pauseRequests();
    }

    @Override
    public void glidePauseRequests(Fragment fragment) {
        GlideApp.with(fragment).pauseRequests();
    }

    @Override
    public void glideResumeRequests(Context context) {
        GlideApp.with(context).resumeRequests();
    }

    @Override
    public void glideResumeRequests(Activity activity) {
        GlideApp.with(activity).resumeRequests();
    }

    @Override
    public void glideResumeRequests(Fragment fragment) {
        GlideApp.with(fragment).resumeRequests();
    }

    /**
     * 加载缩略图
     *
     * @param context
     * @param url           图片url
     * @param backUrl       缩略图的url
     * @param thumbnailSize 如果需要放大放小的数值
     * @param imageView
     */
    @Override
    public void displayImageThumbnail(Context context, String url, String backUrl, int thumbnailSize, ImageView imageView) {

        if (thumbnailSize == 0) {
            GlideApp.with(context)
                    .load(url)
                    .thumbnail(Glide.with(context)
                            .load(backUrl))
                    .into(imageView);
        } else {

            //越小，图片越小，低网络的情况，图片越小
            GlideApp.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//为了测试不缓存
                    .thumbnail(GlideApp.with(context)
                            .load(backUrl)
                            .override(thumbnailSize))// API 来强制 Glide 在缩略图请求中加载一个低分辨率图像
                    .into(imageView);
        }

    }

    @Override
    public void displayImageThumbnail(Activity activity, String url, String backUrl, int thumbnailSize, ImageView imageView) {
        if (thumbnailSize == 0) {
            GlideApp.with(activity)
                    .load(url)
                    .thumbnail(Glide.with(activity)
                            .load(backUrl))
                    .into(imageView);
        } else {

            //越小，图片越小，低网络的情况，图片越小
            GlideApp.with(activity)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//为了测试不缓存
                    .thumbnail(GlideApp.with(activity)
                            .load(backUrl)
                            .override(thumbnailSize))// API 来强制 Glide 在缩略图请求中加载一个低分辨率图像
                    .into(imageView);
        }
    }

    @Override
    public void displayImageThumbnail(Fragment fragment, String url, String backUrl, int thumbnailSize, ImageView imageView) {
        if (thumbnailSize == 0) {
            GlideApp.with(fragment)
                    .load(url)
                    .thumbnail(Glide.with(fragment)
                            .load(backUrl))
                    .into(imageView);
        } else {

            //越小，图片越小，低网络的情况，图片越小
            GlideApp.with(fragment)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//为了测试不缓存
                    .thumbnail(GlideApp.with(fragment)
                            .load(backUrl)
                            .override(thumbnailSize))// API 来强制 Glide 在缩略图请求中加载一个低分辨率图像
                    .into(imageView);
        }
    }

    /**
     * * thumbnail 方法有一个简化版本，它只需要一个 sizeMultiplier 参数。
     * 如果你只是想为你的加载相同的图片，但尺寸为 View 或 Target 的某个百分比的话特别有用：
     *
     * @param fragment
     * @param url
     * @param thumbnailSize
     * @param imageView
     */
    @Override
    public void displayImageThumbnail(Fragment fragment, String url, float thumbnailSize, ImageView imageView) {
        if (thumbnailSize >= 0.0F && thumbnailSize <= 1.0F) {
            GlideApp.with(fragment)
                    .load(url)
                    .thumbnail(/*sizeMultiplier=*/ thumbnailSize)
                    .into(imageView);
        } else {
            throw new IllegalArgumentException("thumbnailSize 的值必须在0到1之间");
        }
    }

    @Override
    public void displayImageThumbnail(Activity activity, String url, float thumbnailSize, ImageView imageView) {
        if (thumbnailSize >= 0.0F && thumbnailSize <= 1.0F) {
            GlideApp.with(activity)
                    .load(url)
                    .thumbnail(/*sizeMultiplier=*/ thumbnailSize)
                    .into(imageView);
        } else {
            throw new IllegalArgumentException("thumbnailSize 的值必须在0到1之间");
        }
    }

    @Override
    public void displayImageThumbnail(Context context, String url, float thumbnailSize, ImageView imageView) {
        if (thumbnailSize >= 0.0F && thumbnailSize <= 1.0F) {
            GlideApp.with(context)
                    .load(url)
                    .thumbnail(/*sizeMultiplier=*/ thumbnailSize)
                    .into(imageView);
        } else {
            throw new IllegalArgumentException("thumbnailSize 的值必须在0到1之间");
        }
    }

    private void mainThreadCallback(final String url, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                if (onProgressListener != null) {
                    onProgressListener.onProgress(url, bytesRead, totalBytes, isDone, exception);
                }

                if (onGlideImageViewListener != null) {
                    onGlideImageViewListener.onProgress(percent, isDone, exception);
                }
            }
        });
    }
}
