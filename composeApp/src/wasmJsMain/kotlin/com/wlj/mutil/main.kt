package com.wlj.mutil

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
//    document.charset = "utf-8"
    ComposeViewport(document.body!!) {
        App{}
    }
}