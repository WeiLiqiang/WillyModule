package com.wlq.willymodule.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApp;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Application app) {
        if (app == null) {
            LogUtils.e("Utils", "app is null.");
            return;
        }
        if (sApp == null) {
            sApp = app;
            UtilsBridge.init(sApp);
            UtilsBridge.preLoad();
            return;
        }
        if (sApp.equals(app)) return;
        UtilsBridge.unInit(sApp);
        sApp = app;
        UtilsBridge.init(sApp);
    }

    public static Application getApp() {
        if (sApp != null) return sApp;
        init(UtilsBridge.getApplicationByReflect());
        if (sApp == null) throw new NullPointerException("reflect failed.");
        LogUtils.i("Utils", UtilsBridge.getCurrentProcessName() + " reflect app success.");
        return sApp;
    }

    public abstract static class Task<Result> extends ThreadUtils.SimpleTask<Result> {

        private Utils.Consumer<Result> mConsumer;

        public Task(final Utils.Consumer<Result> consumer) {
            mConsumer = consumer;
        }

        @Override
        public void onSuccess(Result result) {
            if (mConsumer != null) {
                mConsumer.accept(result);
            }
        }
    }

    public interface OnAppStatusChangedListener {
        void onForeground(Activity activity);

        void onBackground(Activity activity);
    }

    public static class ActivityLifecycleCallbacks {

        public void onActivityCreated(@NonNull Activity activity) {/**/}

        public void onActivityStarted(@NonNull Activity activity) {/**/}

        public void onActivityResumed(@NonNull Activity activity) {/**/}

        public void onActivityPaused(@NonNull Activity activity) {/**/}

        public void onActivityStopped(@NonNull Activity activity) {/**/}

        public void onActivityDestroyed(@NonNull Activity activity) {/**/}

        public void onLifecycleChanged(@NonNull Activity activity, Lifecycle.Event event) {/**/}
    }

    public interface Consumer<T> {
        void accept(T t);
    }

    public interface Supplier<T> {
        T get();
    }

    public interface Func1<Ret, Par> {
        Ret call(Par param);
    }
}
