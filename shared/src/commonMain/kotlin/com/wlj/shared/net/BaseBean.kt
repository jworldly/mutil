package com.wlj.shared.net
interface BaseBean<T> {
    fun getDataInfo(): T
    fun getDataCode(): Int
    fun getDataMsg(): String

}
