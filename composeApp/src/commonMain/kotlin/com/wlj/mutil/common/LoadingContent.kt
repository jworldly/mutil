package com.wlj.mutil.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wlj.shared.state.ComposableUnit
import com.wlj.shared.tools
import com.wlj.shared.viewmodel.CommonState.Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoadingContent(
    state: Loading ,
    coroutine: CoroutineScope = rememberCoroutineScope(),
) {

    tools.log("LoadingContent")

    DisposableEffect(Unit) {
        val job = coroutine.launch {
            delay(state.time?:5000)
            state.block?.invoke(false)
        }
        onDispose {
            job.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        state.msg?.let {
            Text(text = it)
        }
    }

}