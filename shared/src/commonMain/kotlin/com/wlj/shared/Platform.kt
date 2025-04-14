package com.wlj.shared

/**
 * 平台差异获取
 */
interface Platform {

    val name: String

    val randomUUID: String

    fun log(str:String)

    fun toast(str:String)
}

expect fun getPlatform(): Platform


