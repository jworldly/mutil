package com.wlj.mutil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.wlj.mutil.theme.Theme
import com.wlj.mutil.di.AppModule
import com.wlj.mutil.net.headers
import com.wlj.mutil.ui.MainNavGraph
import com.wlj.shared.Config
import com.wlj.shared.di.initKoin
import com.wlj.shared.net.loading.GlobalLoadingDialog
import com.wlj.shared.tools
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.app_name
import mutil.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.mp.KoinPlatform.getKoinOrNull


@OptIn(ExperimentalCoilApi::class)
@Composable
fun App(pop: () -> Unit) {

    Config.apply {
        debug = true
        headersBuilder.headers()
        appName = stringResource(Res.string.app_name)
    }

    if (getKoinOrNull() == null) {
        initKoin(AppModule)
    }

//    //图片预览设置 没用到
//    val previewHandler = AsyncImagePreviewHandler {
//        ColorImage(Color.Red.toArgb())
//    }
//    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
////        AsyncImage(
////            model = "https://example.com/image.jpg",
////            contentDescription = null,
////        )
//    }
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    // 加载loading
    GlobalLoadingDialog()

    Theme {
        Surface {
            MainNavGraph()
        }
    }
}

@Composable
fun test(
    toLogin: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showContent = !showContent }) {
            Text("sjdjkkd 测试 ")
        }
        AnimatedVisibility(showContent) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: ${tools.platformName()}")
                Text("去登录", modifier = Modifier.clickable { toLogin() })
            }
        }
    }
}