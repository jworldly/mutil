package com.wlj.mutil.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wlj.mutil.theme.Theme
import com.wlj.shared.viewmodel.CommonState
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.compose_multiplatform
import mutil.composeapp.generated.resources.state_page_empty
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @Author: wlj
 * @Date: 2025/3/10
 * @Description:空页面
 */
@Composable
fun EmptyContent(
   state: CommonState
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
        Text(state.msg ?: stringResource(Res.string.state_page_empty))
    }
}

@Preview
//@HotPreview
@Composable
private fun PreviewContent() {
    Theme {
        Surface {
            EmptyContent(CommonState.Empty)
        }
    }
}