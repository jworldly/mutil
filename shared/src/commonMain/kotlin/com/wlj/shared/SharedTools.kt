package com.wlj.shared

import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import io.ktor.client.engine.HttpClientEngineConfig
import org.koin.mp.KoinPlatform.getKoin

/**
 * @Author: wlj
 * @Date: 2025/4/17
 * @Description: 多平台工具 集合类
 */
class SharedTools {

    val platform: Platform = getPlatform()

    fun platformName(): String {
        return platform.name
    }

    fun getRandomUUID(): String {
        return platform.randomUUID
    }

    fun log(str: String) {
        if (Config.debug) {
            platform.log(str)
        }
    }

    fun toast(str: String) {
        platform.toast(str)
    }

    fun configEngine(config: HttpClientEngineConfig) {
        platform.configEngine(config)
    }

    val kv: FlowSettings = settings


//    fun flowSettings2Settings(){
//        kv.toBlockingSettings()
//    }

}

val tools: SharedTools by lazy { getKoin().get() }
val kv: FlowSettings by lazy { tools.kv }