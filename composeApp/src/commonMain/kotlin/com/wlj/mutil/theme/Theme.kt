package com.wlj.mutil.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalGraphicsContext

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor  -> {
            val context = LocalGraphicsContext.current
            LightColorScheme
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    bar()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        shapes = CustomShape1,
        content = content
    )
}

@Composable
private fun bar() {
    //    val view = LocalView.current
//    if (!view.isInEditMode) {//不是布局编辑器中查看和编辑布局文件时的状
//        SideEffect {
//            if (view.context is Activity) {
//                val window = (view.context as Activity).window
//                window.statusBarColor = colorScheme.primary.toArgb() //状态栏背景
//                //状态栏文字颜色
//                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//            } else {
//                Log.e(TAG, "view.context is not Activity")
//            }
//        }
//    }
}



