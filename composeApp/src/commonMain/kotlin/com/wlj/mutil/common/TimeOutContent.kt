package com.wlj.mutil.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wlj.shared.tools
import com.wlj.shared.viewmodel.CommonState
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.compose_multiplatform
import mutil.composeapp.generated.resources.state_page_time_out
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimeOutContent(
    state: CommonState.TimeOut
) {

    tools.log("TimeOutContent")

    Column(
        modifier = Modifier.fillMaxSize().clickable { state.block?.invoke(false) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            state.painter ?: painterResource(Res.drawable.compose_multiplatform),
            contentDescription = null
        )
        Text(state.msg ?: stringResource(Res.string.state_page_time_out))

    }

}
