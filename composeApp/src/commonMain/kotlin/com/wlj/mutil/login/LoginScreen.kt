package com.wlj.mutil.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wlj.mutil.StateEffectScaffold
import com.wlj.mutil.toPainterResource
import com.wlj.shared.Greeting
import com.wlj.shared.ListItem
import com.wlj.shared.getPlatform
import mutil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * @Author: wlj
 * @Date: 2025/3/11
 * @Description:
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    pop: () -> Unit,
) {

    StateEffectScaffold(
        viewModel = koinViewModel<LoginVM>(),
        sideEffect = { viewModel, sideEffect ->

        }
    ) { viewModel, state ->
//        when (state) {
//            LoginState.Loading -> Loading()
//            LoginState.Empty -> Empty()
//        }
    }


    Content(
        modifier,
        pop,
        listOf(
            ListItem(
                Res.drawable.ic_login_google,
                Res.string.label_login_google
            ) {
//                viewModel.loginGoogle { pop() }
                getPlatform().toast("loginGoogle")
            },

            ListItem(
                Res.drawable.ic_login_google,
                Res.string.label_login_mail
            ) {
                getPlatform().log("Res.string.label_login_mail")
//                viewModel.loginGoogle { pop() }
            },

            ListItem(
                Res.drawable.ic_login_google,
                Res.string.label_login_facebook
            ) {
                getPlatform().log("Res.string.label_login_phone")
            }
        )
    )
}

@Composable
fun Content(
    modifier: Modifier,
    pop: () -> Unit,
    list: List<ListItem>,
) {

    Box(modifier) {

        Image(
            painter = "ic_login_bg".toPainterResource(),
            contentDescription = "bg",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {

            Image(
                painter = painterResource(Res.drawable.ic_login_top), contentDescription = null,
                Modifier.padding(top = 55.dp)
            )

            Text(
                text = stringResource(Res.string.label_login_1),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
            )

            Text(
                text = stringResource(Res.string.label_login_2),
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface,
            )

            LoginList(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp, 50.dp, 20.dp, 30.dp),
                list
            )

            Spacer(Modifier.weight(1f))
            //隐私
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 45.dp)
            ) {
                Text(
                    text = stringResource(Res.string.label_login_privacy_0),
                    fontSize = 11.sp,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(Res.string.label_login_privacy),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(Res.string.label_login_privacy_1),
                    fontSize = 11.sp,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }

        }

        Icon(
            painter = painterResource(Res.drawable.ic_login_google),
            contentDescription = "close",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 20.dp, top = 20.dp)
                .clickable {
                    pop()
                },
            tint = Color.Unspecified // 关键：禁用着色
        )
    }
}

@Preview
@Composable
fun Test() {
    Content(Modifier, {}, listOf())
}
