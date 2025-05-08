package com.wlj.shared.net

import com.wlj.shared.Config
import com.wlj.shared.net.exception.EmptyException
import com.wlj.shared.net.loading.LoadingManager
import com.wlj.shared.utils.handleFlowErrors
import com.wlj.shared.utils.showLoadingDialog
import com.wlj.shared.state.showStatePage
import com.wlj.shared.viewmodel.BaseVM
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified T> Flow<HttpResponse>.warpResponse(
    loading: LoadingManager? = null,
    crossinline customHandel: (Throwable) -> Boolean = { true },//返回true就继续执行默认错误处理
) = this.let {

    val l = loading?.let { it1 -> it.showLoadingDialog(it1) } ?: it
    l.map {
        Config.netConverter.onConvert<T>(typeInfo<T>(), it)
    }.handleFlowErrors(customHandel)
}

/**
 * 封装网络请求的返回值，并显示加载中、空数据、错误等状态
 *
 * @param refresh 是否刷新,为true时，返回为空显示空状态页
 */
inline fun <reified T> Flow<HttpResponse>.warpResponseState(
    vm: BaseVM<*, *, *>,
    refresh: Boolean,
    crossinline exceptionHandel: (Throwable) -> Boolean = { true },//返回true就继续执行默认错误处理
) = this.let {
    it.showStatePage(vm, exceptionHandel) {
        val t = Config.netConverter.onConvert<T>(typeInfo<T>(), it)
        if (refresh && t is BaseBean<*>) {
            if (t.dataInfo().isEmptyData()) throw EmptyException(it)
        }
        t
    }
}

// 优化为扩展函数
fun Any?.isEmptyData(): Boolean = when {
    this == null -> true
    this is Collection<*> -> isEmpty()
    else -> false
}



