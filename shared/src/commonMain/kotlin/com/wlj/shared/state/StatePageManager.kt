package com.wlj.shared.state

import androidx.compose.runtime.Composable

/**
 * @Author: wlj
 * @Date: 2025/5/6
 * @Description:
 */
class StatePageManager {

    /** 当前缺省页是否加载成功过, 即是否执行过[showContent] */
    var loaded = false

    /** 当前缺省页状态[Status] */
    var status: Status = Status.CONTENT
        private set

    // <editor-fold desc="设置缺省页">

    /** 加载页页面布局 */
    var loadingComposable: ComposableUnit? = null
        get() = if (field == null) StateConfig.loadingComposable else field
        set(value) {
            if (field != value) {
                removeStatus()
                field = value
            }
        }

    /** 错误页面布局 */
    var errorComposable: ComposableUnit? = null
        get() = if (field == null) StateConfig.errorComposable else field
        set(value) {
            if (field != value) {
                removeStatus()
                field = value
            }
        }

    /** 错误页面布局 */
    var emptyComposable: ComposableUnit? = null
        get() = if (field == null) StateConfig.emptyComposable else field
        set(value) {
            if (field != value) {
                removeStatus()
                field = value
            }
        }

    // </editor-fold desc="设置缺省页">

    /** 缓存用的，暂时没使用。移除状态 */
    private fun removeStatus() {

    }


    private var onEmpty: (Status.(tag: Any?) -> Unit)? = null
        get() = field ?: StateConfig.onEmpty
    private var onError: (Status.(tag: Any?) -> Unit)? = null
        get() = field ?: StateConfig.onError
    private var onContent: (Status.(tag: Any?) -> Unit)? = null
        get() = field ?: StateConfig.onContent
    private var onLoading: (Status.(tag: Any?) -> Unit)? = null
        get() = field ?: StateConfig.onLoading
    private var onRefresh: (Status.(tag: Any?) -> Unit)? = null


    // <editor-fold desc="监听缺省页">

    /**
     * 当加载中缺省页显示时回调
     * @see showLoading
     * @see StateConfig.onLoading
     */
    fun onLoading(block: Status.(tag: Any?) -> Unit) = apply {
        onLoading = block
    }

    /**
     * 当空缺省页显示时回调
     * @see showEmpty
     * @see StateConfig.onEmpty
     */
    fun onEmpty(block: Status.(tag: Any?) -> Unit) = apply {
        onEmpty = block
    }

    /**
     * 当错误缺省页显示时回调
     * @see showError
     * @see StateConfig.onError
     */
    fun onError(block: Status.(tag: Any?) -> Unit) = apply {
        onError = block
    }

    /**
     * 当[showLoading]时会回调该函数参数, 一般将网络请求等异步操作放入其中
     */
    fun onRefresh(block: Status.(tag: Any?) -> Unit) = apply {
        onRefresh = block
    }

    /**
     * 当[showContent]时会回调该函数参数, 一般将网络请求等异步操作放入其中
     * @see showContent
     * @see StateConfig.onContent
     */
    fun onContent(block: Status.(tag: Any?) -> Unit) = apply {
        onContent = block
    }

    // </editor-fold>

    // <editor-fold desc="显示缺省页">

    /**
     *
     * @param tag 传递任意对象给[onLoading]函数
     * @param silent 仅执行[onRefresh], 不会显示加载中布局, 也不执行[onLoading]
     * @param refresh 是否回调[onRefresh]
     */
    @Composable
    fun showLoading(
        status: Status,
        tag: Any? = null,
        silent: Boolean = false,
        refresh: Boolean = true,
    ) {
        // 静默刷新
        if (silent && refresh) {
            onRefresh?.invoke(status, tag)
            return
        }
        //在loading页面，刷新onLoading
        if (this.status == Status.LOADING) {
            onLoading?.invoke(this.status, tag)
            return
        }
        showStatus(status, tag)
        if (refresh) onRefresh?.invoke(status, tag)
    }

    /**
     * 显示空页, 会触发[onEmpty]的函数参数
     * @param tag 传递任意对象给[onEmpty]函数
     */
    @Composable
    fun showEmpty(status: Status, tag: Any? = null) {
        showStatus(status, tag)
    }

    /**
     * 显示错误页, 会触发[onError]的函数参数
     * @param tag 传递任意对象给[onError]函数
     */
    @Composable
    fun showError(status: Status, tag: Any? = null) {
        if (!loaded) showStatus(status, tag)
    }

    /**
     * 显示内容布局, 表示成功缺省页
     * @param tag 传递任意对象给[onContent]函数
     */
    @Composable
    fun showContent(status: Status, tag: Any? = null) {
        showStatus(status, tag)
        loaded = true
    }

    // </editor-fold ">

    @Composable
    private fun showStatus(status: Status, tag: Any?) {
        if (this.status == status) return
        this.status = status


        when (status) {

            Status.CONTENT -> onContent?.invoke(status, tag)

            Status.LOADING -> {
                onLoading?.invoke(status, tag)
                loadingComposable?.invoke(tag)
            }

            Status.EMPTY -> {
                onEmpty?.invoke(status, tag)
                emptyComposable?.invoke(tag)
            }

            Status.ERROR -> {
                onError?.invoke(status, tag)
                errorComposable?.invoke(tag)
            }
        }
    }


}