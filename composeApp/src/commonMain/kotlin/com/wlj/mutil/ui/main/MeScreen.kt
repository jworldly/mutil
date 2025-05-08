package com.wlj.mutil.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wlj.mutil.ui.MainNavigationActions
import com.wlj.shared.tools

/**
 * @Author: wlj
 * @Date: 2025/4/19
 * @Description:
 */
@Composable
fun MeScreen(nav: MainNavigationActions) {
    var count = rememberSaveable { mutableStateOf(0) }
    var count1 = remember { mutableStateOf(0) }
    tools.log("me ${count.value}")

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("to login", modifier = Modifier.clickable { nav.toLogin() })

        //点击追加次数的按钮
        Button(onClick = { count.value++ }) {
            Text("count:${count.value}", color = Color.White)
        }

        //点击追加次数的按钮
        Button(onClick = { count1.value++ }) {
            Text("count1:${count1.value}", color = Color.White)
        }
    }
}