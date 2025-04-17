package com.wlj.shared.utils

import com.wlj.shared.Config
import com.wlj.shared.net.loading.LoadingManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**封装通用异常处理的扩展函数*/
inline fun <T> Flow<T>.handleFlowErrors(
    crossinline customHandel: (Throwable) -> Boolean = { true } //返回true就继续执行
) = catch { ex ->
    if (customHandel(ex)) Config.errorHandler.onError(ex)
}

/**
 * 显示加载框.onStart与onCompletion外面还可以用
 */
inline fun <T> Flow<T>.showLoadingDialog(
    loading: LoadingManager
) = onStart {
    loading.showLoading()
}.onCompletion {  // 无论成功/异常都会触发
    loading.hideLoading()
}