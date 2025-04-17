package com.wlj.shared.net

import com.wlj.shared.net.exception.RequestParamsException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

interface NetConverter {

    @Throws(Throwable::class)
    suspend fun <R> onConvert(baseBean: BaseBean<R>, response: HttpResponse): R

    companion object DEFAULT : NetConverter {
        /**
         * 返回结果应当等于泛型对象, 可空
         * @param typeBean 请求要求返回的泛型类型
         * @param response 请求响应对象
         */
        override suspend fun <R> onConvert(baseBean: BaseBean<R>, response: HttpResponse): R {
            if (!response.status.isSuccess()) throw ServerResponseException(
                response,
                "${response.status.value}"
            )
            // 根据返回的code码进行判断
            when (val code = baseBean.getDataCode()) {
                in 200..299 -> {
                    return baseBean.getDataInfo()
                }

                in 400..499 -> {
                    throw RequestParamsException(response, baseBean.getDataMsg())
                }

                else -> {
                    throw ServerResponseException(response, baseBean.getDataMsg()) // 服务器异常错误
                }
            }
        }
    }
}