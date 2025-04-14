package com.wlj.mutil

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wlj.mutil.login.LoginScreen
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.label_login_1
import org.jetbrains.compose.resources.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen(
                pop = {
                    //
                    Log.d("TAG", "onCreate: finish")
                    finish()
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    LoginScreen {

    }
}
