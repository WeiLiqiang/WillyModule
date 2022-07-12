package com.wlq.willymodule.common.http.retrofit

import com.wlq.willymodule.base.business.network.BaseRetrofitClient
import com.wlq.willymodule.base.business.network.cookie.CookieJarImpl
import com.wlq.willymodule.base.business.network.cookie.store.PersistentCookieStore
import com.wlq.willymodule.base.business.network.https.HttpsUtils
import com.wlq.willymodule.base.business.network.interceptor.BaseInterceptor
import com.wlq.willymodule.base.business.network.interceptor.CacheInterceptor
import com.wlq.willymodule.base.util.Utils
import com.wlq.willymodule.common.http.constant.HttpConstants
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

open class CommonRetrofitClient(private val headers: Map<String, String>?) : BaseRetrofitClient() {

    constructor() : this(null)

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com"
    }

    open var addCacheInterceptor: Boolean = true

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        if (addCacheInterceptor) {
            val cacheFile = File(Utils.getApp().cacheDir, "cache")
            val cache = Cache(cacheFile, HttpConstants.MAX_CACHE_SIZE)
            builder.cookieJar(CookieJarImpl(PersistentCookieStore()))
            builder.addInterceptor(CacheInterceptor()).cache(cache)
        }

        if (!headers.isNullOrEmpty()) {
            builder.addInterceptor(BaseInterceptor(headers))
        }

        val sslParams = HttpsUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
    }

    override fun handleService(retrofitBuilder: Retrofit.Builder) {
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        retrofitBuilder.baseUrl(BASE_URL)
    }
}