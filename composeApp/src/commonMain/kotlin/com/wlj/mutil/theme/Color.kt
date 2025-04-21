package com.wlj.mutil.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

//Dark
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)


val test = Color(red = 73, green = 69, blue = 79)

//=========Scheme ==================

val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

val blue =  Color(0xFF0058FA)
val purple =  Color(0xFF9A4AF5) //紫色
val black =  Color(0xFF333333) //灰色
val grayBlack =  Color(0xFF787878) //灰黑
//val gray =  Color(0xFF999999) //灰
val grayWhite =  Color(0xFFC3C3CB) //灰白

val LightColorScheme = lightColorScheme(
    primary = blue,    // 主要品牌颜色
    secondary = Color(0xFFF4AE3F),  // 次要品牌颜色
    tertiary = purple,  // 第三品牌颜色
    background = Color(0xFFF7F8FA), // 背景颜色
    surface = Color.White,                // 表面颜色（如卡片）
//    surfaceBright = Color(red = 254, green = 247, blue = 255),
    surfaceContainer = Color(0xFFDBDBDB), //按钮没选中
//    surfaceContainerHigh =
//    surfaceContainerHighest =
//    surfaceContainerLow =
//    surfaceContainerLowest =
//    surfaceDim =

    onPrimary = Color.White,  // 放置在 primary 背景上的前景颜色
//    onSecondary = Color.Black,
//    onTertiary = Color.White,
//    onBackground = Color(0xFFC3C3CB),
    onSurface = Color.Black,
    onSurfaceVariant = grayBlack, // 文字颜色 灰黑
//    inversePrimary = Color(0xFFF3F7FA),
//    inverseSurface = Color(0xFF98989D),
    inverseOnSurface = grayWhite, //文字没选中 灰白

)