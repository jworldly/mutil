package com.wlj.mutil.ui.login

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wlj.mutil.StateEffectScaffold
import com.wlj.mutil.toPainterResource
import com.wlj.mutil.ui.MainNavigationActions
import com.wlj.mutil.ui.login.LoginEffect.NavigationToHost
import com.wlj.mutil.ui.login.LoginState
import com.wlj.shared.ui.ListItem
import com.wlj.shared.tools
import com.wlj.shared.viewmodel.CommonState
import de.drick.compose.hotpreview.HotPreview
import kotlinx.coroutines.delay
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.ic_login_google
import mutil.composeapp.generated.resources.ic_login_top
import mutil.composeapp.generated.resources.label_login_1
import mutil.composeapp.generated.resources.label_login_2
import mutil.composeapp.generated.resources.label_login_facebook
import mutil.composeapp.generated.resources.label_login_google
import mutil.composeapp.generated.resources.label_login_mail
import mutil.composeapp.generated.resources.label_login_privacy
import mutil.composeapp.generated.resources.label_login_privacy_0
import mutil.composeapp.generated.resources.label_login_privacy_1
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * @Author: wlj
 * @Date: 2025/3/11
 * @Description:
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navigationActions: MainNavigationActions,
    pop: () -> Unit,
) {
    val vm :LoginVM = koinViewModel()

    StateEffectScaffold(
        vm,
        LoginState,
        sideEffect = { vm, effect ->
            when (effect) {
                is NavigationToHost -> {
                    navigationActions.close()
                }
            }
        }
    ) { vm, state ->

        Content(
            modifier, pop, listOf(
                ListItem(
                    Res.drawable.ic_login_google, Res.string.label_login_google
                ) {
                    vm.sendAction(LoginAction.Google)
                },

                ListItem(
                    Res.drawable.ic_login_google, Res.string.label_login_mail
                ) {
                    vm.sendAction(LoginAction.VerifyCode)
                },

                ListItem(
                    Res.drawable.ic_login_google, Res.string.label_login_facebook
                ) {
                    vm.sendAction(LoginAction.OnLoginClicked)
                })
        )

    }
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
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        ) {

            Image(
                painter = painterResource(Res.drawable.ic_login_top),
                contentDescription = null,
                Modifier.padding(top = 55.dp)
            )

            Text(
                text = stringResource(Res.string.label_login_1),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
            )

            Text(
                text = stringResource(Res.string.label_login_2),
                fontSize = 11.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
            )

            LoginList(
                Modifier.fillMaxSize().padding(20.dp, 50.dp, 20.dp, 30.dp), list
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
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(Res.string.label_login_privacy),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(Res.string.label_login_privacy_1),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }

        }

        Icon(
            painter = painterResource(Res.drawable.ic_login_google),
            contentDescription = "close",
            modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding()
                .padding(end = 20.dp, top = 20.dp).clickable {
                    pop()
                },
            tint = Color.Unspecified // 关键：禁用着色
        )
    }
}

@HotPreview(widthDp = 411, heightDp = 891, density = 2.625f)
@Composable
fun Test() {

    Content(
        Modifier, {},
        listOf(
            ListItem(
                Res.drawable.ic_login_google, Res.string.label_login_mail
            ) {
                tools.toast("Res.string.label_login_mail 事实上 ")
            },

            ListItem(
                Res.drawable.ic_login_google, Res.string.label_login_facebook
            ) {

            }

        )
    )
}
