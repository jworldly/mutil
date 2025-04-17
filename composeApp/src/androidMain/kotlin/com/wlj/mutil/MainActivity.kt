package com.wlj.mutil

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wlj.mutil.ui.login.Content
import com.wlj.mutil.ui.login.LoginScreen
import com.wlj.mutil.ui.login.Test
import com.wlj.shared.ListItem
import com.wlj.shared.getPlatform
import com.wlj.shared.net.loading.GlobalLoadingDialog
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.ic_login_google
import mutil.composeapp.generated.resources.label_login_1
import mutil.composeapp.generated.resources.label_login_google
import org.jetbrains.compose.resources.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                pop = {
                    //
                    Log.d("TAG", "onCreate: finish")
                    finish()
                }
            )
            GlobalLoadingDialog()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    Test()
}
