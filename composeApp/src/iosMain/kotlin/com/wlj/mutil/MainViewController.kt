package com.wlj.mutil

import androidx.compose.ui.window.ComposeUIViewController
import com.wlj.mutil.login.LoginScreen

fun MainViewController() = ComposeUIViewController {
    LoginScreen {
//        exitApplication()
    }

}