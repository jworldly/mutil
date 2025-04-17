package com.wlj.shared.di

import com.wlj.shared.Config
import com.wlj.shared.SharedTools
import com.wlj.shared.net.loading.LoadingManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * 初始化koin
 */
fun initKoin(appModule: Module): KoinApplication {

    return startKoin {
        modules(
            listOf(
                sharedModule,
                netModule,
                appModule,
            )
        )
    }
}

val netModule = module {
    single {
        HttpClient() {
            //默认url/请求头
            defaultRequest {
                url(Config.baseUrl)
                headers { Config.configHeaders(this) }
            }
            //重试 https://ktor.io/docs/client-request-retry.html#conditions
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)//retrying a request if a 5xx response
                exponentialDelay() //字数延迟
            }
            //序列化与反序列化
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true  /*忽略未知字段*/ })
            }
            //超时
            install(HttpTimeout){
                requestTimeoutMillis = 5_000
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 5_000
            }

//            //缓存
//            install(HttpCache) //Cache-Control

//            //Response validation
//            expectSuccess = true
//            HttpResponseValidator { //验证响应
//                //2xx response
//                validateResponse { response ->
//                    val error: Error = response.body()
//                    if (error.code != 200) {
//                        throw ServerResponseException(
//                            response,
//                            "Code: ${error.code}, message: ${error.message}"
//                        )
//                    }
//                }
//                //non-2xx
//                handleResponseExceptionWithRequest { exception, request ->
//                    val clientException = exception as? ClientRequestException
//                        ?: return@handleResponseExceptionWithRequest
//                    val exceptionResponse = clientException.response
//                    if (exceptionResponse.status == HttpStatusCode.NotFound) {
//                        val exceptionResponseText = exceptionResponse.bodyAsText()
//                        throw MissingPageException(exceptionResponse, exceptionResponseText)
//                    }
//                }
//            }
            //日志
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        get<SharedTools>().log(message)
                    }
                }
                level = LogLevel.ALL
            }
            //配置网络请求引擎
            engine { get<SharedTools>().configEngine(this) }
        }
    }

    single { LoadingManager() }
}

val sharedModule = module {
    single { SharedTools() }
}