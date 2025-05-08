package com.wlj.shared.net

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonElement

/**
 * 包装成Flow
 *
 *  [更多请求方式](https://ktor.io/docs/client-requests.html#form_parameters)
 *
 *  T 解析，返回T
 *
 *
 */
fun HttpClient.warpPostFlow(
    path: String,
    vararg pair: Pair<String, JsonElement>,
): Flow<HttpResponse> {
    return flow<HttpResponse> {
        val response = warpPost(path, *pair)
        emit(response)
    }
}

suspend fun HttpClient.warpPost(
    path: String,
    vararg pair: Pair<String, JsonElement>
): HttpResponse {
    return post(path) {
        contentType(ContentType.Application.Json)//传对象转json要设置这个
        setBody(mapOf(*pair))
    }
}


/**
 * [文件上传] (https://ktor.io/docs/client-requests.html#upload_file)
 *
 * 两种方式
 */
suspend fun upFile() {
    // TODO: 文件上传
}



