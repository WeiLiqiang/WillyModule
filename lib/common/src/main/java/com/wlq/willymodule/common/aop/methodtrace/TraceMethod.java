package com.wlq.willymodule.common.aop.methodtrace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface TraceMethod {

    boolean trace() default true;

    boolean traceInnerMethod() default false;
}
