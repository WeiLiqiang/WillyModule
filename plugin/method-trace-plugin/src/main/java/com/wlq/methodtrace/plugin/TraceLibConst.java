package com.wlq.methodtrace.plugin;

public interface TraceLibConst {

    String PACKAGE = "com/wlq/willymodule/common/aop/methodtrace/";

    /**
     * 注解
     */
    String ANNOTATION_CLASS = PACKAGE + "TraceClass";
    String ANNOTATION_METHOD = PACKAGE + "TraceMethod";

    /**
     * trace 统一处理
     */
    String CLASS_RECORD = PACKAGE + "TraceRecord";
    String CLASS_RECORD_METHOD_DESC = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V";
    String CLASS_RECORD_METHOD_I = "onMethodEnter";
    String CLASS_RECORD_METHOD_O = "onMethodEnd";
}
