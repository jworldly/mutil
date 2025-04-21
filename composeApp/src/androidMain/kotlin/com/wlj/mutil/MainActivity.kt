package com.wlj.mutil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wlj.mutil.ui.login.Test

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(pop = { finish() })
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    Test()
}
