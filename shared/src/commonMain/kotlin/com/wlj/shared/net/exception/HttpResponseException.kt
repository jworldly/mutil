package com.wlj.shared.net.exception

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.cio.Response

/**
 * 该类表示Http请求在服务器响应成功后失败
 * @param response 响应信息
 * @param message 错误描述信息
 * @param cause 错误原因
 *
 * @see ResponseException HttpStatusCode 200...299
 * @see RequestParamsException HttpStatusCode 400...499
 * @see ServerResponseException HttpStatusCode 500...599
 */
open class HttpResponseException(
    open val response: HttpResponse,
    message: String? = null,
    cause: Throwable? = null
) : NetException(response.request, message, cause)