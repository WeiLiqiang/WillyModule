package com.wlq.willymodule.base.apm.dyn.callback;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import com.wlq.willymodule.base.apm.dyn.bean.DynamicSoInfo;

public interface DynamicSoCallback {

    void onSuccess(@NonNull DynamicSoInfo info);

    void onProgress(@IntRange(from = 0, to = 100) int progress);

    void onFail();
}
