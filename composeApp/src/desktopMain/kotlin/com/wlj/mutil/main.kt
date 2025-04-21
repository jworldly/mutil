package com.wlj.mutil

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Mutil",
    ) {
        App(
            pop = { exitApplication() }
        )
    }
}

