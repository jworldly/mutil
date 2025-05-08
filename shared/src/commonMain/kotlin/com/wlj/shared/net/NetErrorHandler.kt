package com.wlj.shared.net;

import com.wlj.shared.net.exception.NetException
import com.wlj.shared.net.exception.RequestParamsException
import com.wlj.shared.net.exception.ResponseException
import com.wlj.shared.net.exception.ServerResponseException
import com.wlj.shared.tools
import io.ktor.client.plugins.ClientRequestException

interface NetErrorHandler {


    companion object DEFAULT : NetErrorHandler

    /**
     * 全局的网络错误处理
     *
     * @param e 发生的错误
     */
    fun onError(e: Throwable) {
        val message = when (e) {

            is RequestParamsException -> e.message
            is ServerResponseException -> e.message
            is NullPointerException -> e.message
            is ResponseException -> e.message
            is NetException -> e.message
            else -> e.message
        }

        tools.toast(message.toString())
        tools.log("onError $e")
    }

    /**
     * 当你使用包含缺省页功能的作用域中发生错误将回调本函数处理错误
     *
     * @param e 发生的错误
     */
    fun onStateError(e: Throwable) {
        when (e) {
            is ClientRequestException,
            is RequestParamsException,
            is ResponseException,
            is NullPointerException -> onError(e)

            else ->  tools.log("onStateError $e")
        }
    }
}
