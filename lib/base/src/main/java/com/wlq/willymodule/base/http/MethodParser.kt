package com.wlq.willymodule.base.http

import com.wlq.willymodule.base.http.annotation.*
import java.lang.reflect.*

class MethodParser(
    private val baseUrl: String,
    method: Method
) {

    companion object {

        fun parse(baseUrl: String, method: Method): MethodParser {
            return MethodParser(baseUrl, method)
        }
    }

    private var replaceRelativeUrl: String? = null
    private var domainUrl: String? = null
    private var formPost: Boolean = true
    private var httpMethod: Int = -1
    private lateinit var relativeUrl: String
    private lateinit var returnType: Type
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var parameters: MutableMap<String, String> = mutableMapOf()

    init {

        //解析方法的注解参数如get, headers, post, baseUrl
        parseMethodAnnotations(method)

        //解析方法的返回类型
        parseMethodReturnType(method)
    }

    fun newRequest(method: Method, args: Array<out Any>?): ArcRequest {

        val arguments: Array<Any> = args as Array<Any>? ?: arrayOf()
        parseMethodParameters(method, arguments)
        val request = ArcRequest()
        request.domainUrl = domainUrl
        request.returnType = returnType
        request.relativeUrl = replaceRelativeUrl ?: relativeUrl
        request.parameters = parameters
        request.headers = headers
        request.httpMethod = httpMethod
        request.formPost = formPost
        return request
    }

    /**
     * interface ApiService {
     *      fun listCities(@Path("province") province: Int,@Filed("page") page: Int): ArcCall<JsonObject>
     * }
     */
    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        //每次调用API接口时，都是需要把上一次解析到的参数清理掉，因为methodParser存在复用
        parameters.clear()

        //例如 @Path("province") province: Int, @Filed("page") page: Int
        val parameterAnnotations = method.parameterAnnotations
        val equals = parameterAnnotations.size == args.size
        require(equals) {
            String.format(
                "arguments annotations count %s dont match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        //args
        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) { "filed can only has one annotation :index =$index" }
            val value = args[index]
            require(isPrimitive(value)) {
                "8 basic types are supported for now,index=$index"
            }

            val annotation = annotations[0]
            if (annotation is Filed) {
                val key = annotation.value
                val value = args[index]
                parameters[key] = value.toString()
            } else if (annotation is Path) {
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    //relativeUrl = home/{categroyId}
                    replaceRelativeUrl = relativeUrl.replace("${replaceName}", replacement)
                }
            } else {
                throw  IllegalStateException("cannot handle parameter annotation :" + annotation.javaClass.toString())
            }
        }
    }
    /**
     * interface ApiService {
     *  @Headers("auth-token:token", "accountId:123456")
     *  @BaseUrl("https://api.devio.org/as/")
     *  @POST("/cities/{province}")
     *  @GET("/cities")
     * fun listCities(@Path("province") province: Int,@Filed("page") page: Int): ArcCall<JsonObject>
     * }
     */
    private fun parseMethodAnnotations(method: Method) {

        val annotations = method.annotations
        for (annotation in annotations) {
            when (annotation) {
                is GET -> {
                    relativeUrl = annotation.value
                    httpMethod = ArcRequest.METHOD.GET
                }
                is POST -> {
                    relativeUrl = annotation.value
                    httpMethod = ArcRequest.METHOD.POST
                    formPost = annotation.formPost
                }
                is Headers -> {
                    val headersArray = annotation.value
                    for (header in headersArray) {
                        val colon = header.indexOf(":")
                        check(!(colon == 0 || colon == -1)) {
                            String.format(
                                "@headers value must be in the form [name:value] ,but found [%s]",
                                header
                            )
                        }
                        val name = header.substring(0, colon)
                        val value = header.substring(colon + 1).trim()
                        headers[name] = value
                    }
                }
                is BaseUrl -> {
                    domainUrl = annotation.value
                }
                else -> {
                    throw IllegalStateException("cannot handle method annotation:" + annotation.javaClass.toString())
                }
            }
        }
        require((httpMethod == ArcRequest.METHOD.GET) || (httpMethod == ArcRequest.METHOD.POST)) {
            String.format("method %s must has one of GET,POST ", method.name)
        }
        if (domainUrl == null) {
            domainUrl = baseUrl
        }
    }

    /**
     * interface ApiService {
     *  @Headers("auth-token:token", "accountId:123456")
     *  @BaseUrl("https://api.devio.org/as/")
     *  @POST("/cities/{province}")
     *  @GET("/cities")
     * fun listCities(@Path("province") province: Int,@Filed("page") page: Int): ArcCall<JsonObject>
     * }
     */
    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != ArcCall::class.java) {
            throw IllegalStateException(
                String.format(
                    "method %s must be type of ArcCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) {
                { "method %s can only has one generic return type" }
            }
            val argument = actualTypeArguments[0]
            require(validateGenericType(argument)) {
                String.format("method %s generic return type must not be an unknown type. " + method.name)
            }
            returnType = argument
        } else {
            throw  IllegalStateException(
                String.format(
                    "method %s must has one generic return type", method.name
                )
            )
        }
    }

    private fun validateGenericType(type: Type): Boolean {

        /**
         * wrong
         *  fun test():ArcCall<Any>
         *  fun test():ArcCall<List<*>>
         *  fun test():ArcCall<ApiInterface>
         *
         * expect
         *  fun test():ArcCall<User>
         */

        //如果指定的泛型是集合类型的，还需要检查集合的泛型参数
        if (type is GenericArrayType) {
            return validateGenericType(type.genericComponentType)
        }

        //如果指定的泛型是一个接口，也不行
        if (type is TypeVariable<*>) {
            return false
        }

        //如果指定的泛型是一个通配符 ？ extends Number也不行
        if (type is WildcardType) {
            return false
        }

        return true
    }

    private fun isPrimitive(value: Any): Boolean {

        //String
        if (value.javaClass == String::class.java) {
            return true
        }
        try {
            //int byte short long boolean char double float
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return false
    }
}