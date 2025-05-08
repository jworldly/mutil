package com.wlj.shared.state

import androidx.compose.runtime.Composable

typealias ComposableUnit = @Composable (Any?) -> Unit

/**
 * @Author: wlj
 * @Date: 2025/5/6
 * @Description:
 */
object StateConfig {


    var loadingComposable: ComposableUnit? = null

    var errorComposable: ComposableUnit? = null

    var emptyComposable: ComposableUnit? = null


    internal var onLoading: (Status.(Any?) -> Unit)? = null
    internal var onContent: (Status.(Any?) -> Unit)? = null
    internal var onEmpty: (Status.(Any?) -> Unit)? = null
    internal var onError: (Status.(Any?) -> Unit)? = null

    fun onLoading(block: Status.(Any?) -> Unit) {
        onLoading = block
    }

    fun onContent(block: Status.(Any?) -> Unit) {
        onContent = block
    }

    fun onEmpty(block: Status.(Any?) -> Unit) {
        onEmpty = block
    }

    fun onError(block: Status.(Any?) -> Unit) {
        onError = block
    }

}