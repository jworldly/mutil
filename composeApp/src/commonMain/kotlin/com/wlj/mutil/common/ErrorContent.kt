package com.wlj.mutil.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wlj.shared.viewmodel.CommonState
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

/**
 * @Author: wlj
 * @Date: 2025/3/10
 * @Description:错误页面
 */
@Composable
fun ErrorContent(
    state: CommonState.Error
) {

    Column(
        modifier = Modifier.fillMaxSize().clickable { state.block?.invoke(false) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = state.painter ?: painterResource(Res.drawable.compose_multiplatform),
            contentDescription = "空",
            modifier = Modifier.size(96.dp)
        )

        Text(state.msg ?: "错误")
    }
}
