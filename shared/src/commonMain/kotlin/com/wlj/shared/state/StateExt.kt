package com.wlj.shared.state

import com.wlj.shared.Config
import com.wlj.shared.net.BaseBean
import com.wlj.shared.net.exception.EmptyException
import com.wlj.shared.net.isEmptyData
import com.wlj.shared.tools
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.CommonState
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart


/**
 * 状态页
 */
inline fun <R> Flow<HttpResponse>.showStatePage(
    vm: BaseVM<*, *, *>,
    crossinline customException: (Throwable) -> Boolean = { true }, //返回true就继续执行默认异常
    crossinline map: suspend (HttpResponse) -> R
) = onStart {
//    vm.emitCommonState(CommonState.Loading().apply {
//        block = { vm.emitCommonState { CommonState.TimeOut } }
//    })
}.map {
    map(it)
}.onCompletion {
    tools.log("showStatePage-onCompletion $it")
    if (it is EmptyException) {
        vm.emitCommonState(CommonState.Empty)
    } else if (it == null) {
        vm.emitCommonState(CommonState.Content)
    }
}.catch {
    tools.log("showStatePage-catch $it")
    //外部处理异常
    if (!customException(it)) return@catch
    // 默认处理方式
    when (it) {
        is EmptyException -> {}

        is HttpRequestTimeoutException,
        is ConnectTimeoutException,
        is SocketTimeoutException -> vm.emitCommonState(CommonState.TimeOut)

        else -> {
            Config.errorHandler.onStateError(it)
            vm.emitCommonState(CommonState.Error(e = it))
        }
    }
}