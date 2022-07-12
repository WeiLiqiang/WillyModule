package com.wlq.willymodule.common.aop.methodtrace;

public interface IMethodTrace {

    void onMethodEnter(String className, String metodName, String methodDesc, String outerMethod);

    void onMethodEnd(String className, String methodName, String methodDesc, String outerMethod);
}
