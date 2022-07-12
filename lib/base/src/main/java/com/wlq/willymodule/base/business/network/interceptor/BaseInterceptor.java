package com.wlq.willymodule.base.business.network.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements Interceptor {

    private final Map<String, String> headers;

    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, Objects.requireNonNull(headers.get(headerKey))).build();
            }
        }
        //请求信息
        return chain.proceed(builder.build());
    }
}
