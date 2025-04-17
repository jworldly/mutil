package com.wlj.shared.net.loading

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoadingManager {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //AtomicInteger 确保线程安全
    private val count = atomic(0)

    fun showLoading() {
        val fetch = count.incrementAndGet()
        if (fetch > 0) _isLoading.value = true
    }

    fun hideLoading() {
        val current = count.decrementAndGet()
        if (current <= 0) {
            _isLoading.value = false
        }
    }

// 方案二：使用 synchronized 同步块
//    private var count: Int = 0
//
//    fun showLoading() = synchronized(this) {
//        count++
//        if (count > 0) _isLoading.value = true
//    }
//
//    fun hideLoading() = synchronized(this) {
//        count--
//        if (count <= 0) {
//            _isLoading.value = false
//        }
//    }

}
