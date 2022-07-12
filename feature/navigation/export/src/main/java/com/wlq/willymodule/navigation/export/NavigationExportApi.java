package com.wlq.willymodule.navigation.export;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentTransaction;

import com.wlq.willymodule.base.util.ApiUtils;

public abstract class NavigationExportApi extends ApiUtils.BaseApi {

    public abstract void showFragment(FragmentTransaction transaction, @IdRes int containerViewId);

    public abstract void hideFragment(FragmentTransaction transaction);

    public abstract void removeFragment(FragmentTransaction transaction);

    public abstract void destroy();
}
