package com.wlj.mutil

import androidx.compose.ui.window.ComposeUIViewController
import com.wlj.mutil.di.AppModule
import com.wlj.mutil.ui.login.LoginScreen
import com.wlj.shared.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin(AppModule)
    App {
//        exitApplication()
    }

}