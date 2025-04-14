package com.wlj.shared

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import java.util.UUID
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class AndroidPlatform : Platform {

    private val context: Context by inject(Context::class.java)

    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val randomUUID: String
        get() = UUID.randomUUID().toString()

    override fun log(str: String) {
        Log.d("TAG", str)
    }

    override fun toast(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

}

actual fun getPlatform(): Platform = AndroidPlatform()