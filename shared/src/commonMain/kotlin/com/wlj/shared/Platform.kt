package com.wlj.shared

import io.ktor.client.engine.HttpClientEngineConfig

/**
 * 平台差异获取
 */
interface Platform {

    val name: String

    val randomUUID: String

    fun log(str: String)

    fun toast(str: String)

    /**
     * android的HttpClient日志加拦截器
     */
    fun configEngine(config: HttpClientEngineConfig)
}

expect fun getPlatform(): Platform


