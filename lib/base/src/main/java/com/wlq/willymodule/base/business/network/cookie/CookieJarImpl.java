package com.wlq.willymodule.base.business.network.cookie;

import androidx.annotation.NonNull;

import com.wlq.willymodule.base.business.network.cookie.store.CookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieJarImpl implements CookieJar {

    private final CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null!");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        cookieStore.saveCookie(url, cookies);
    }

    @NonNull
    @Override
    public synchronized List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return cookieStore.loadCookie(url);
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
