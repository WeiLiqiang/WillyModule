package com.wlq.willymodule.base.business.network.cookie.store;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.wlq.willymodule.base.util.LogUtils;
import com.wlq.willymodule.base.util.SPUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieStore implements CookieStore {

    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "habit_cookie";        //cookie使用prefs保存
    private static final String COOKIE_NAME_PREFIX = "cookie_";          //cookie持久化的统一前缀

    private final HashMap<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final SPUtils spUtils;

    public PersistentCookieStore() {
        spUtils = SPUtils.getInstance(COOKIE_PREFS);
        cookies = new HashMap<>();

        //将持久化的cookies缓存到内存中,数据结构为 Map<Url.host, Map<Cookie.name, Cookie>>
        Map<String, ?> prefsMap = spUtils.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if ((entry.getValue()) != null && !entry.getKey().startsWith(COOKIE_NAME_PREFIX)) {
                //获取url对应的所有cookie的key,用","分割
                String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    //根据对应cookie的Key,从xml中获取cookie的真实值
                    String encodedCookie = spUtils.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        Cookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!cookies.containsKey(entry.getKey()))
                                cookies.put(entry.getKey(), new ConcurrentHashMap<>());
                            Objects.requireNonNull(cookies.get(entry.getKey())).put(name, decodedCookie);
                        }
                    }
                }
            }
        }
    }

    private String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     * 当前cookie是否过期
     */
    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    /**
     * 根据当前url获取所有需要的cookie,只返回没有过期的cookie
     */
    @NonNull
    @Override
    public List<Cookie> loadCookie(HttpUrl url) {
        ArrayList<Cookie> ret = new ArrayList<>();
        assert url != null;
        if (cookies.containsKey(url.host())) {
            Collection<Cookie> urlCookies = Objects.requireNonNull(cookies.get(url.host())).values();
            for (Cookie cookie : urlCookies) {
                if (isCookieExpired(cookie)) {
                    removeCookie(url, cookie);
                } else {
                    ret.add(cookie);
                }
            }
        }
        return ret;
    }

    /**
     * 将url的所有Cookie保存在本地
     */
    @Override
    public void saveCookie(HttpUrl url, List<Cookie> urlCookies) {
        if (!cookies.containsKey(url.host())) {
            cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        for (Cookie cookie : urlCookies) {
            //当前cookie是否过期
            if (isCookieExpired(cookie)) {
                removeCookie(url, cookie);
            } else {
                saveCookie(url, cookie, getCookieToken(cookie));
            }
        }
    }

    @Override
    public void saveCookie(HttpUrl url, Cookie cookie) {
        if (!cookies.containsKey(url.host())) {
            cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        //当前cookie是否过期
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie);
        } else {
            saveCookie(url, cookie, getCookieToken(cookie));
        }
    }

    /**
     * 保存cookie，并将cookies持久化到本地,数据结构为
     * Url.host -> Cookie1.name,Cookie2.name,Cookie3.name
     * cookie_Cookie1.name -> CookieString
     * cookie_Cookie2.name -> CookieString
     */
    private void saveCookie(HttpUrl url, Cookie cookie, String name) {
        //内存缓存
        Objects.requireNonNull(cookies.get(url.host())).put(name, cookie);
        spUtils.put(url.host(), TextUtils.join(",", Objects.requireNonNull(cookies.get(url.host())).keySet()));
        spUtils.put(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableHttpCookie(cookie)));
    }

    /**
     * 根据url移除当前的cookie
     */
    @Override
    public boolean removeCookie(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);
        assert url != null;
        if (cookies.containsKey(url.host()) && Objects.requireNonNull(cookies.get(url.host())).containsKey(name)) {
            //内存移除
            Objects.requireNonNull(cookies.get(url.host())).remove(name);
            if (spUtils.contains(COOKIE_NAME_PREFIX + name)) {
                spUtils.remove(COOKIE_NAME_PREFIX + name);
            }
            spUtils.put(url.host(), TextUtils.join(",", Objects.requireNonNull(cookies.get(url.host())).keySet()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeCookie(HttpUrl url) {
        assert url != null;
        if (cookies.containsKey(url.host())) {
            //文件移除
            Set<String> cookieNames = Objects.requireNonNull(cookies.get(url.host())).keySet();
            for (String cookieName : cookieNames) {
                if (spUtils.contains(COOKIE_NAME_PREFIX + cookieName)) {
                    spUtils.remove(COOKIE_NAME_PREFIX + cookieName);
                }
            }
            spUtils.remove(url.host());
            //内存移除
            cookies.remove(url.host());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeAllCookie() {
        spUtils.clear();
        cookies.clear();
        return true;
    }

    /**
     * 获取所有的cookie
     */
    @NonNull
    @Override
    public List<Cookie> getAllCookie() {
        List<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet())
            ret.addAll(Objects.requireNonNull(cookies.get(key)).values());
        return ret;
    }

    @NonNull
    @Override
    public List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        assert url != null;
        Map<String, Cookie> mapCookie = cookies.get(url.host());
        if (mapCookie != null) ret.addAll(mapCookie.values());
        return ret;
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    private String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null) return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            LogUtils.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }
        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    private Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getCookie();
        } catch (IOException e) {
            LogUtils.d(LOG_TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            LogUtils.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        }
        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
