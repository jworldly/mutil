package com.wlj.shared.utils

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
 *
 * 当至少有一个订阅者的时候启动，最后一个订阅者停止订阅之后还能继续保持stopTimeoutMillis时间的活跃，之后才停止。
 *
 * replayExpirationMillis直接翻译过来是重播过期时间，默认是Long.MAX_VALUE，当取消协程之后，这个缓存的值需要保留多久，
 * 如果是0，表示立马就过期，并把shareIn运算符的缓存值设置为initialValue初始值。
 */
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
