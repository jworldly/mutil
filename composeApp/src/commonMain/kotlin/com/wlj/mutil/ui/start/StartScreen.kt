package com.wlj.mutil.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wlj.mutil.ui.MainNavigationActions

/**
 * @Author: wlj
 * @Date: 2025/4/19
 * @Description:
 */
@Composable
fun StartScreen(modifier: Modifier, navActions: MainNavigationActions, click: () -> Unit) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = { navActions.toMain(1) }) {
            Text("start main", color = Color.White)
        }

        Button(
            onClick = { navActions.toLogin() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("start Login", color = Color.White)
        }
    }
}