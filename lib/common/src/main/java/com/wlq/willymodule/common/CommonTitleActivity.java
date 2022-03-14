package com.wlq.willymodule.common;

import android.annotation.SuppressLint;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

public abstract class CommonTitleActivity extends CommonBackActivity {

    public abstract CharSequence bindTitle();

    protected FrameLayout commonTitleContentView;

    @Override
    public boolean isSwipeBack() {
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public void setRootLayout(@LayoutRes int layoutId) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
