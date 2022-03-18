package com.wlq.willymodule.common.aop.methodtrace.custom;

import androidx.core.os.TraceCompat;

import com.wlq.willymodule.common.aop.methodtrace.IMethodTrace;

public class CustomSystemTrace implements IMethodTrace {

    @Override
    public void onMethodEnter(String className, String methodName, String methodDesc, String outerMethod) {
        TraceCompat.beginSection(className + "#" + methodName);
    }

    @Override
    public void onMethodEnd(String className, String methodName, String methodDesc, String outerMethod) {
        TraceCompat.endSection();
    }
}
