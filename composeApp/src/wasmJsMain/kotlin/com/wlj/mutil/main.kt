package com.wlj.mutil

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.wlj.mutil.di.AppModule
import com.wlj.mutil.ui.login.LoginScreen
import com.wlj.shared.di.initKoin
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(AppModule)
    ComposeViewport(document.body!!) {
        App{}
    }
}