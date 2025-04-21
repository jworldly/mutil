package com.wlj.shared.net.loading

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.koin.compose.koinInject

/**
 * 默认全局加载对话框
 */
@Composable
fun GlobalLoadingDialog() {
    val loadingManager: LoadingManager = koinInject()

    val isLoading by loadingManager.isLoading.collectAsState()

    Content(isLoading) { // 点击取消按钮
        loadingManager.hideLoading()
    }
}

@Composable
private fun Content(isLoading: Boolean, onDismissRequest: () -> Unit) {

    if (isLoading) {
        Dialog(
            onDismissRequest = { onDismissRequest() },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            CircularProgressIndicator()
        }
    }
}