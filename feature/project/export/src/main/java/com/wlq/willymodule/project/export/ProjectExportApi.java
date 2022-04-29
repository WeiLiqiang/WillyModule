package com.wlq.willymodule.project.export;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.wlq.willymodule.base.util.ApiUtils;

public abstract class ProjectExportApi extends ApiUtils.BaseApi {

    public abstract void showFragment(@Nullable FragmentTransaction transaction, @IdRes int containerViewId);

    public abstract void hideFragment(@Nullable FragmentTransaction transaction);

    public abstract void removeFragment(@Nullable FragmentTransaction transaction);

    public abstract void destroy();
}
