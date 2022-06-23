package com.wlq.willymodule.base.business.network

import com.wlq.willymodule.base.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {

    companion object CLIENT {
        private const val TIME_OUT = 5
    }

    private val client: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        handleBuilder(builder)
        builder.addInterceptor(getHttpLoggingInterceptor())
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        return logging
    }

    abstract fun handleBuilder(builder: OkHttpClient.Builder)

    abstract fun handleService(retrofitBuilder: Retrofit.Builder)

    open fun <Service> getService(serviceClass: Class<Service>): Service {
        return getService(serviceClass, baseUrl = null)
    }

    open fun <Service> getService(serviceClass: Class<Service>, baseUrl: String?): Service {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(client)
        handleService(retrofitBuilder)
        if (baseUrl != null) {
            retrofitBuilder.baseUrl(baseUrl)
        }
        return retrofitBuilder.build().create(serviceClass)
    }
}