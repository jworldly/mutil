package com.wlj.mutil.net

import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders
import kotlin.random.Random

/**
 * @Author: wlj
 * @Date: 2025/4/15
 * @Description: 请求头组装
 */

fun HeadersBuilder.headers() {
    append("tm", Random.nextLong().toString())
    append("deviceNumber", "deviceNumber")
    append("auth", "")
    append("deviceBrand", "BRAND")
    append("deviceModel", "Build.MODEL")
    append("deviceSystemVersion", "Build.VERSION.SDK_INT.toString()")
    append("appVersion", "AppUtils.getAppVersionName()")
    append("platform", "ANDROID")
    append("channel", "OW")
//    append(HttpHeaders.CacheControl, "OW")
}
