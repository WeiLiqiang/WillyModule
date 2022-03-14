package com.wlq.willymodule.common.http.callfactory

import com.wlq.willymodule.base.http.*
import com.wlq.willymodule.common.http.GsonConvert
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class OkhttpCallFactory(baseUrl: String) : ArcCall.Factory {

    private val okHttpClient: OkHttpClient
    private val gsonConvert: GsonConvert

    companion object {
        const val CONNECT_TIME_OUT = 30L
        const val READ_TIME_OUT = 30L
        const val WRITE_TIME_OUT = 30L
        const val CALL_TIME_OUT = 30L
    }

    init {
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .callTimeout(CALL_TIME_OUT, TimeUnit.SECONDS)
            .build()
        gsonConvert = GsonConvert()
    }

    override fun newCall(request: ArcRequest): ArcCall<Any> {
        return OkhttpCall(request)
    }

    internal inner class OkhttpCall<T>(private val request: ArcRequest) : ArcCall<T> {

        override fun execute(): ArcResponse<T> {
            val realCall = createRealCall()
            val response = realCall.execute()
            return parseResponse(response)
        }

        override fun enqueue(callback: ArcCallback<T>) {
            val realCall = createRealCall()
            realCall.enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(parseResponse(response))
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onFailed(Throwable(e))
                }
            })
        }

        private fun createRealCall(): Call {
            when (request.httpMethod) {
                ArcRequest.METHOD.GET -> {
                    val requestBuilder = Request.Builder()
                        .url(request.endPointUrl())
                    request.headers!!.forEach {
                        requestBuilder.addHeader(it.key, it.value)
                    }
                    val request = requestBuilder.build()
                    return okHttpClient.newCall(request)
                }
                ArcRequest.METHOD.POST -> {
                    val requestBuilder = Request.Builder()
                        .url(request.endPointUrl())
                    val parameters = request.parameters
                    val headers = request.headers
                    headers?.forEach {
                        requestBuilder.addHeader(it.key, it.value)
                    }
                    val jsonObject = JSONObject()
                    val formBodyBuilder = FormBody.Builder()
                    parameters?.forEach {
                        if (request.formPost) {
                            formBodyBuilder.add(it.key, it.value)
                        } else {
                            jsonObject.put(it.key, it.value)
                        }
                    }
                    val requestBody = if (request.formPost) {
                        formBodyBuilder.build()
                    } else {
                        jsonObject.toString()
                            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                    }
                    val request = requestBuilder.post(requestBody).build()
                    return okHttpClient.newCall(request)
                }
                else -> {
                    throw IllegalStateException("只支持get,post请求")
                }
            }
        }

        private fun parseResponse(response: Response): ArcResponse<T> {

            var rawData: String? = null
            if (response.isSuccessful) {
                val body = response.body
                if (body != null) {
                    rawData = body.string()
                }
            } else {
                val body = response.body
                if (body != null) {
                    rawData = body.string()
                }
            }
            return gsonConvert.convert(rawData!!, request.returnType!!)
        }
    }
}