package com.wlq.willymodule.common.http

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.wlq.willymodule.base.http.ArcConvert
import com.wlq.willymodule.base.http.ArcResponse
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class GsonConvert : ArcConvert {

    override fun <T> convert(rawData: String, dataType: Type): ArcResponse<T> {
        val response: ArcResponse<T> = ArcResponse()

        try {
            val jsonObject = JSONObject(rawData)
            response.code = jsonObject.optInt("errorCode")
            response.msg = jsonObject.optString("errorMsg")
            val data = jsonObject.optString("data")
            if (response.code == ArcResponse.SUCCESS) {
                response.data = GsonUtils.fromJson(data, dataType)
            } else {
                response.errorData = GsonUtils.fromJson<MutableMap<String, String>>(
                    data,
                    object : TypeToken<MutableMap<String, String>>() {}.type
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            response.code = -1
            response.msg = e.message
        }
        response.rawData = rawData
        return response
    }

}