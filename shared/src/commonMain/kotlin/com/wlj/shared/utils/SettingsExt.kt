package com.wlj.shared.utils

import com.russhwolf.settings.coroutines.FlowSettings
import com.wlj.shared.Config
import com.wlj.shared.net.loading.LoadingManager
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.CommonState
import com.wlj.shared.viewmodel.State
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json

/**
 * 为空返回默认值
 */
suspend inline fun <reified T> FlowSettings.decodeBean(
    key: String,
    defaultValue: T
): T = getStringOrNull(key)?.let {
    Json.decodeFromString<T>(it)
} ?: defaultValue


/**
 * 没默认值，返回可能为null
 */
suspend inline fun <reified T> FlowSettings.decodeBeanOrNull(
    key: String,
): T? = getStringOrNull(key)?.let {
    Json.decodeFromString<T>(it)
}

/**
 * 获取FlowSettings中的serialization对象值
 */
inline fun <reified T> FlowSettings.decodeBeanFlow(
    key: String,
    defaultValue: T
): Flow<T> = getStringOrNullFlow(key).map {
    it?.let {
        Json.decodeFromString<T>(it)
    } ?: defaultValue
}

/**
 * 设置FlowSettings中的serialization对象
 */
suspend inline fun <reified T> FlowSettings.encodeBean(
    key: String,
    value: T,
) = putString(key, Json.encodeToString(value))

