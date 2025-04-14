package com.wlj.mutil

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.wlj.mutil.login.LoginScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Mutil",
    ) {
        LoginScreen(
            pop = { exitApplication() }
        )
    }
}