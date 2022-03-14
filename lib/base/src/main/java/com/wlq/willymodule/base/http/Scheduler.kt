package com.wlq.willymodule.base.http

/**
 * 代理CallFactory创建出来的call对象，从而实现拦截器的派发动作
 */
class Scheduler(
    private val callFactory: ArcCall.Factory,
    private val interceptors: MutableList<ArcInterceptor>
) {

    fun newCall(request: ArcRequest): ArcCall<*> {
        val realCall: ArcCall<*> = callFactory.newCall(request)
        return ProxyCall(realCall, request)
    }

    internal inner class ProxyCall<T>(
        private val realCall: ArcCall<T>,
        private val request: ArcRequest
    ): ArcCall<T> {

        override fun execute(): ArcResponse<T> {
            dispatchInterceptor(request, null)

            val response = realCall.execute()

            dispatchInterceptor(request, response)

            return response
        }

        override fun enqueue(callback: ArcCallback<T>) {
            dispatchInterceptor(request, null)

            realCall.enqueue(object : ArcCallback<T> {

                override fun onSuccess(response: ArcResponse<T>) {
                    dispatchInterceptor(request, response)

                    callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callback.onFailed(throwable)
                }
            })
        }

        private fun dispatchInterceptor(request: ArcRequest, response: ArcResponse<T>?) {
            if (interceptors.size == 0)
                return
            InterceptorChain(request, response).dispatch()
        }

        internal inner class InterceptorChain(
            private val request: ArcRequest,
            private val response: ArcResponse<T>?
        ): ArcInterceptor.Chain {

            var callIndex: Int = 0

            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): ArcRequest {
                return request
            }

            override fun response(): ArcResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex ++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }
        }
    }
}