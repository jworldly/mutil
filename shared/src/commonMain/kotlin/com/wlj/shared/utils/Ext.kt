package com.wlj.shared.utils

import com.russhwolf.settings.coroutines.FlowSettings
import com.wlj.shared.Config
import com.wlj.shared.net.loading.LoadingManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json

/**封装通用异常处理的扩展函数*/
inline fun <T> Flow<T>.handleFlowErrors(
    crossinline customHandel: (Throwable) -> Boolean = { true } //返回true就继续执行
) = catch { ex ->
    if (customHandel(ex)) Config.errorHandler.onError(ex)
}

/**
 * 显示加载框.onStart与onCompletion外面还可以用
 */
inline fun <reified T> Flow<T>.showLoadingDialog(
    loading: LoadingManager
) = onStart {
    loading.showLoading()
}.onCompletion {  // 无论成功/异常都会触发
    loading.hideLoading()
}

/**
 * 获取FlowSettings中的serialization对象值
 */
suspend inline fun <reified T> FlowSettings.decodeValue(
    key: String,
    defaultValue: T
): T = getStringOrNull(key)?.let {
    Json.decodeFromString<T>(it)
} ?: defaultValue

/**
 * 获取FlowSettings中的serialization对象值
 */
inline fun <reified T> FlowSettings.decodeValueFlow(
    key: String,
    defaultValue: T
): Flow<T> =  getStringOrNullFlow(key).map {
    it?.let {
        Json.decodeFromString<T>(it)
    } ?: defaultValue
}

/**
 * 设置FlowSettings中的serialization对象
 */
suspend inline fun <reified T> FlowSettings.encodeValue(
    key: String,
    value: T,
) = putString(key, Json.encodeToString(value))

