package com.wlj.shared

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    fun getRandomUUID(): String {
        return platform.randomUUID
    }

    fun log(str:String){
        platform.log(str)
    }

    fun toast(str:String){
        platform.toast(str)
    }
}