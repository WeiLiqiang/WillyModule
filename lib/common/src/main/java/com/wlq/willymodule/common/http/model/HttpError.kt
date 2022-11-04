package com.wlq.willymodule.common.http.model

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.squareup.moshi.JsonDataException
import com.wlq.willymodule.base.R
import com.wlq.willymodule.base.util.StringUtils
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.lang.IllegalArgumentException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

enum class HttpError(var code: Int, var errorMsg: String) {

    UNAUTHORIZED(401, "操作未授权"),

    FORBIDDEN(403, "请求被拒绝"),

    NOT_FOUND(404, "资源不存在"),

    REQUEST_TIMEOUT(408, "服务器执行超时"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    SERVICE_UNAVAILABLE(503, "服务器不可用"),

    /**
     * 未知错误
     */
    UNKNOWN(1001, StringUtils.getString(R.string.network_request_unknown_error)),

    /**
     * 解析错误
     */
    PARSE_ERROR(1002, StringUtils.getString(R.string.network_request_parse_error)),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1003, StringUtils.getString(R.string.network_request_connect_error)),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, StringUtils.getString(R.string.network_request_ssl_error)),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1005, StringUtils.getString(R.string.network_request_timeout_error)),

    /**
     * 连接错误
     */
    CONNECT_ERROR(1006, StringUtils.getString(R.string.network_request_timeout_error)),

    /**
     * 未知主机错误
     */
    UNKNOWN_HOST_ERROR(1007, "未知主机错误"),

    /**
     * 非法参数错误
     */
    ILLEGAL_ARGUMENT_ERROR(1008, "非法参数错误"),

    /**
     * 非法参数错误
     */
    ILLEGAL_STATE_ERROR(1009, "状态错误，请排查网络数据处理流程"),

    /**
     * token过期
     */
    TOKEN_EXPIRE(3001, "token过期错误"),

    /**
     * 参数异常
     */
    PARAMS_ERROR(4003, "参数错误");

    fun getValue(): String {
        return errorMsg
    }

    fun getKey(): Int {
        return code
    }
}

internal fun handlingException(e: Throwable): HttpError = when (e) {
    is HttpException -> {
        handlingHttpException(e)
    }
    is SocketTimeoutException -> {
        HttpError.TIMEOUT_ERROR
    }
    is ConnectTimeoutException -> {
        HttpError.TIMEOUT_ERROR
    }
    is ConnectException -> {
        HttpError.CONNECT_ERROR
    }
    is JsonParseException -> {
        HttpError.PARSE_ERROR
    }
    is JSONException -> {
        HttpError.PARSE_ERROR
    }
    is ParseException -> {
        HttpError.PARSE_ERROR
    }
    is MalformedJsonException -> {
        HttpError.PARSE_ERROR
    }
    is JsonDataException -> {
        HttpError.PARSE_ERROR
    }
    is SSLException -> {
        HttpError.SSL_ERROR
    }
    is UnknownHostException -> {
        HttpError.UNKNOWN_HOST_ERROR
    }
    is IllegalArgumentException -> {
        HttpError.ILLEGAL_ARGUMENT_ERROR
    }
    is java.lang.IllegalStateException -> {
        HttpError.ILLEGAL_STATE_ERROR
    }
    else -> {
        HttpError.UNKNOWN
    }
}

internal fun handlingHttpException(exception: HttpException): HttpError = when (exception.code()) {
    HttpError.UNAUTHORIZED.code -> {
        HttpError.UNAUTHORIZED
    }
    HttpError.FORBIDDEN.code -> {
        HttpError.FORBIDDEN
    }
    HttpError.NOT_FOUND.code -> {
        HttpError.NOT_FOUND
    }
    HttpError.REQUEST_TIMEOUT.code -> {
        HttpError.REQUEST_TIMEOUT
    }
    HttpError.INTERNAL_SERVER_ERROR.code -> {
        HttpError.INTERNAL_SERVER_ERROR
    }
    HttpError.SERVICE_UNAVAILABLE.code -> {
        HttpError.SERVICE_UNAVAILABLE
    }
    else -> {
        HttpError.NETWORK_ERROR
    }
}