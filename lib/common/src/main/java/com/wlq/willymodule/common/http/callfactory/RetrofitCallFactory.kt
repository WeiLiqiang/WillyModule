package com.wlq.willymodule.common.http.callfactory

import com.wlq.willymodule.base.http.*
import com.wlq.willymodule.common.http.GsonConvert
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

class RetrofitCallFactory(baseUrl: String) : ArcCall.Factory {

    private val apiService: ApiService
    private val gsonConvert: GsonConvert

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        gsonConvert = GsonConvert()
    }

    override fun newCall(request: ArcRequest): ArcCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(private val request: ArcRequest) : ArcCall<T> {

        override fun execute(): ArcResponse<T> {
            val realCall = createRealCall()
            val response = realCall.execute()
            return parseResponse(response)
        }

        override fun enqueue(callback: ArcCallback<T>) {
            val realCall = createRealCall()
            realCall.enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    callback.onSuccess(parseResponse(response))
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailed(t)
                }
            })
        }

        private fun createRealCall(): Call<ResponseBody> {
            when (request.httpMethod) {
                ArcRequest.METHOD.GET -> {
                    return apiService.get(
                        request.headers,
                        request.endPointUrl(),
                        request.parameters
                    )
                }
                ArcRequest.METHOD.POST -> {
                    val parameters = request.parameters
                    val builder = FormBody.Builder()
                    val jsonObject = JSONObject()
                    parameters?.forEach {
                        if (request.formPost) {
                            builder.add(it.key, it.value)
                        } else {
                            jsonObject.put(it.key, it.value)
                        }
                    }
                    val requestBody = if (request.formPost) {
                        builder.build()
                    } else {
                        jsonObject.toString()
                            .toRequestBody("application/json;utf-8".toMediaType())
                    }
                    return apiService.post(request.headers, request.endPointUrl(), requestBody)
                }
                else -> {
                    throw IllegalStateException("只支持get,post请求")
                }
            }
        }

        private fun parseResponse(response: Response<ResponseBody>): ArcResponse<T> {

            var rawData: String? = null
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    rawData = body.string()
                }
            } else {
                val body = response.errorBody()
                if (body != null) {
                    rawData = body.string()
                }
            }
            return gsonConvert.convert(rawData!!, request.returnType!!)
        }
    }

    interface ApiService {

        @GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>?
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>
    }
}