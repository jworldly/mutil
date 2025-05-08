package com.wlj.mutil.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import com.wlj.mutil.ui.MainNavigationActions
import com.wlj.shared.di.sharedModule
import com.wlj.shared.tools
import de.drick.compose.hotpreview.HotPreview
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.ic_login_top
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication

/**
 * @Author: wlj
 * @Date: 2025/4/19
 * @Description:
 */
@Composable
fun HomeScreen(nav: MainNavigationActions) {
    tools.log("home")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()          // 处理键盘弹起
            .systemBarsPadding()   // 处理状态栏
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        Text("to start", modifier = Modifier.clickable { nav.toStart() })
        //用它封装的组件
        AsyncImage(
            model = "https://hbimg.huaban.com/85177d749ddbf92c7dab1ceda4efb6a8dada91641132b7-VtearC_fw658",
            contentDescription = null,
            modifier = Modifier.clickable {
                tools.toast("sss")
            }.clip(CircleShape).size(400.dp),
            placeholder = painterResource(Res.drawable.ic_login_top),
            contentScale = ContentScale.Crop
        )

        //原生组件
        val painter =
            rememberAsyncImagePainter("https://img0.baidu.com/it/u=3263643750,36156034&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1066")
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        //状态管理
        stateHandle(painter)

        //1、这个函数的主要缺点是它不能检测图像在屏幕上加载的尺寸，总是以原始尺寸加载图像，可以传递一个自定义的 SizeResolver，或者使用 rememberConstraintsSizeResolver（这是 AsyncImage 在内部使用的）来解决这个问题,
        //2、在首次组合时，AsyncImagePainter 的状态总是 AsyncImagePainter.State.Empty（空状态）
        val sizeResolver = rememberConstraintsSizeResolver()
        val painter1 = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data("https://img0.baidu.com/it/u=3263643750,36156034&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1066")
                .size(sizeResolver)
                .build(),
        )
        var text by remember { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painter1,
            contentDescription = null,
            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(8.dp)).then(sizeResolver),
            contentScale = ContentScale.Crop
        )

        //只有在需要观察 AsyncImagePainter.state，并且不能像使用 rememberAsyncImagePainter 那样在第一次组合和第一帧时让其状态为 Empty 的情况下，当前函数（SubcomposeAsyncImage）才有用。
        SubcomposeAsyncImage(
            model = "https://gips3.baidu.com/it/u=2361057328,3546065477&fm=3039&app=3039&f=JPEG?w=1024&h=1024",
//            loading = {
//                CircularProgressIndicator()
//            },
            contentDescription = null,
        ) {
            val state = this.painter.state.collectAsState().value
            if (state is AsyncImagePainter.State.Success) {
                SubcomposeAsyncImageContent()
            } else {
                CircularProgressIndicator()
            }
        }
        Text("底部")
    }
}

/**
 * 根据AsyncImagePainter.state收集状态，根据状态显示不同的UI
 */
@Composable
private fun stateHandle(painter: AsyncImagePainter) {
    val state by painter.state.collectAsState()
    when (state) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator()
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = null
            )
        }

        is AsyncImagePainter.State.Error -> {
            // Show some error UI.
        }
    }
}

@HotPreview(widthDp = 411, heightDp = 891, density = 2.625f)
@Composable
private fun Test() {
    KoinApplication(
        application = {
            modules(sharedModule)
        }
    ) {
        HomeScreen(
            MainNavigationActions(rememberNavController())
        )
    }
}