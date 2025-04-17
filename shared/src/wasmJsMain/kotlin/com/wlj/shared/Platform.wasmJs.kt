package com.wlj.shared

import io.ktor.client.engine.HttpClientEngineConfig

//这个注解用于指定 Kotlin 代码中 crypto 变量对应 JavaScript 中的 crypto 对象。
@JsName("crypto")
external val crypto: Crypto

//这个接口定义了 crypto 对象的 randomUUID 方法
external interface Crypto {
    @JsName("randomUUID")
    fun randomUUID(): String
}

@JsName("console")
external val console: Console

external interface Console {
    @JsName("log")
    fun log(message: String)
}

@JsName("alert")
external fun alert(message: String)

class WasmPlatform: Platform {

    override val name: String = "Web with Kotlin/Wasm"
    override val randomUUID: String
        get() =  crypto.randomUUID()

    override fun log(str: String) {
        console.log(str)
    }

    override fun toast(str: String) {
        alert(str)
    }

    override fun configEngine(config: HttpClientEngineConfig) {

    }

}

actual fun getPlatform(): Platform = WasmPlatform()