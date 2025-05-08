package com.wlj.shared.state

import androidx.compose.runtime.Composable
import com.wlj.shared.tools

/**
 * @Author: wlj
 * @Date: 2025/4/29
 * @Description: 通用状态页
 */
@Composable
fun pageState(
    builder: StatePageManager,
    status: Status = Status.LOADING,
    tag: Any? = null
) {
    tools.log("pageState 重组,Status:$status")

    when (status) {
        Status.LOADING -> builder.showLoading(status, tag)
        Status.EMPTY -> builder.showEmpty(status, tag)
        Status.ERROR -> builder.showError(status, tag)
        Status.CONTENT -> builder.showContent(status, tag)
    }

}




