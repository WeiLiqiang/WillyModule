package com.wlq.willymodule.common.aop.methodtrace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TraceClass {

    Class methodTrace() default TimeTrace.class;

    boolean traceAllMethod() default false;

    boolean traceInnerMethod() default false;
}
