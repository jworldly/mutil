package com.wlj.shared.net
interface BaseBean<T> {
    fun dataInfo(): T
    fun dataCode(): Int
    fun dataMsg(): String

}
