package com.wlj.shared.net

import com.wlj.shared.Config
import com.wlj.shared.utils.handleFlowErrors
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonElement

/**
 * 包装成Flow
 *
 *  [更多请求方式](https://ktor.io/docs/client-requests.html#form_parameters)
 *
 *  T 解析，返回T
 */
inline fun <reified T> HttpClient.warpPostFlow(
    path: String,
    vararg pair: Pair<String, JsonElement>,
    crossinline customHandel: (Throwable) -> Boolean = { true } //返回true就继续执行
): Flow<T> {
    return flow<T> {
        val response = warpPost(path, *pair)
        emit(response.body())
    }.handleFlowErrors(customHandel)
}

/**
 * 用BaseBean<T> 解析，返回T
 */
inline fun <reified T, reified baseBean : BaseBean<T>> HttpClient.warpPostFlowBaseBean(
    path: String,
    vararg pair: Pair<String, JsonElement>,
    crossinline customHandel: (Throwable) -> Boolean = { true } //返回true就继续执行
): Flow<T> {
    return flow {
        val response = warpPost(path, *pair)

        val body: baseBean = response.body()
        emit(Config.netConverter.onConvert(body, response))
    }.handleFlowErrors(customHandel)
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
