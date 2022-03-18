package com.wlq.methodtrace.plugin;

import com.ss.android.ugc.bytex.common.BaseExtension;

public class TraceExtension extends BaseExtension {

    public String defaultMethodTraceClass;

    public String defaultMethodTraceClass() {
        return defaultMethodTraceClass;
    }

    public void defaultMethodTraceClass(String defaultMethodTraceClass) {
        this.defaultMethodTraceClass = defaultMethodTraceClass;
    }

    @Override
    public String getName() {
        return "TracePlugin";
    }
}
