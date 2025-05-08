package com.wlj.shared.net.exception

import io.ktor.client.statement.HttpResponse

/**
 * 给状态页显示空用
 */
class EmptyException(
    response: HttpResponse,
    message: String? = null,
    cause: Throwable? = null,
    var tag: Any? = null
) : HttpResponseException(response, message, cause)