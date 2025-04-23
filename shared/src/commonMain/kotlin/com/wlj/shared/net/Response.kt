package com.wlj.shared.net

import com.wlj.shared.net.loading.LoadingManager
import com.wlj.shared.utils.handleFlowErrors
import com.wlj.shared.utils.showLoadingDialog
import kotlinx.coroutines.flow.Flow

inline fun <reified T> Flow<T>.warpResponse(
    loading: LoadingManager? = null,
    crossinline customHandel: (Throwable) -> Boolean = { true },//返回true就继续执行
) = this.let {
    val f = loading?.let { it1 -> it.showLoadingDialog(it1) } ?: it
    f.handleFlowErrors(customHandel)
}



