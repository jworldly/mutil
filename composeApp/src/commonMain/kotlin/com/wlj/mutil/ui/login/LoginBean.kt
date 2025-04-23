package com.wlj.mutil.ui.login


import kotlinx.serialization.Serializable


@Serializable
data class LoginBean(
    val auth: String? = null,
    val bindId: String? = null,
    val bindPhone: Boolean? = null,
    val firstLogin: Boolean? = null,
    val loginType: String? = null,
    val originalData: String? = null
)

