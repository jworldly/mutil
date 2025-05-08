package com.wlj.shared.ui

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * @Author: wlj
 * @Date: 2025/3/11
 * @Description:
 */

data class ListItem(
    val icon: DrawableResource,
    val title: StringResource,
    val onClick: () -> Unit
)