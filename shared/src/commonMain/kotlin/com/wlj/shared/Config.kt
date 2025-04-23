package com.wlj.shared

import com.wlj.shared.net.NetConverter
import com.wlj.shared.net.NetErrorHandler
import io.ktor.http.HeadersBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * @Author: wlj
 * @Date: 2025/4/14
 * @Description:
 */
object Config {

    /** 全局域名 */

    //    var host: String = "app.mystiai.com"
    var baseUrl: String = "http://192.168.1.2:9102"

    /** 是否启用日志 */
    var debug = true

    /** 网络异常日志的标签 */
    var TAG = "NET_LOG"

    /** 错误处理器 */
    var errorHandler: NetErrorHandler = NetErrorHandler
    /// 网络请求转换器
    var netConverter: NetConverter = NetConverter

    /**
     * 请求头组装
     */
    var headersBuilder = HeadersBuilder()
    val headers by lazy { headersBuilder.build() }

    fun configHeaders(headersBuilder: HeadersBuilder) {
        if (headers.isEmpty()) throw IllegalArgumentException("headers is empty")
        headersBuilder.appendAll(headers)
    }

    var appName = ""

}

