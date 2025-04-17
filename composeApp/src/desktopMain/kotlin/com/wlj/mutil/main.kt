package com.wlj.mutil

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.wlj.mutil.di.AppModule
import com.wlj.mutil.ui.login.LoginScreen
import com.wlj.shared.di.initKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun main() = application {
    initKoin(AppModule)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Mutil",
    ) {
        App(
            pop = { exitApplication() }
        )
    }
}