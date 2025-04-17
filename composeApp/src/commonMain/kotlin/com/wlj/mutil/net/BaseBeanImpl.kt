package com.wlj.mutil.net

import com.wlj.shared.net.BaseBean
import kotlinx.serialization.Serializable

@Serializable
data class BaseBeanImpl<T>(
    val code: Int = 0,
    val message: String = "",
    @Serializable
    val `data`: T,
) : BaseBean<T> {
    override fun getDataInfo() = data

    override fun getDataCode() = code

    override fun getDataMsg() = message

}