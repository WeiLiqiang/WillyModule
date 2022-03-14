package com.wlq.willymodule.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class TouchView extends ViewGroup {

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
