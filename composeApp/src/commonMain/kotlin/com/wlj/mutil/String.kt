package com.wlj.mutil

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalInspectionMode
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.reflect.KProperty

/**
 * @Author: wlj
 * @Date: 2025/4/9
 * @Description:
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun String.toPainterResource(): Painter {
//    if(LocalInspectionMode.current){
    val drawableResource = Res.allDrawableResources[this]?:run {
        throw IllegalArgumentException("$this is not found")
    }
    return painterResource(drawableResource)
}

