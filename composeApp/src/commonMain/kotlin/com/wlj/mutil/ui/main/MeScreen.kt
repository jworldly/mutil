package com.wlj.mutil.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wlj.shared.tools

/**
 * @Author: wlj
 * @Date: 2025/4/19
 * @Description:
 */
@Composable
fun MeScreen(modifier: Modifier, click: () -> Unit) {
    tools.log("me")
    Text("to login", modifier = modifier.clickable {click()})
}