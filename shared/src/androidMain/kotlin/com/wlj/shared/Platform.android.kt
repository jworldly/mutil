package com.wlj.shared

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.nerdythings.okhttp.profiler.OkHttpProfilerInterceptor
import java.util.UUID
import org.koin.java.KoinJavaComponent.inject

val appContext: Context by inject(Context::class.java)
class AndroidPlatform : Platform {


    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val randomUUID: String
        get() = UUID.randomUUID().toString()

    override fun log(str: String) {
        Log.d(Config.TAG, str)
    }

    override fun toast(str: String) {
        Toast.makeText(appContext, str, Toast.LENGTH_SHORT).show()
    }

    override fun configEngine(config: HttpClientEngineConfig) {
        if (!Config.debug) return
        if (config !is OkHttpConfig) return
        config.addInterceptor(OkHttpProfilerInterceptor())
    }

}

actual fun getPlatform(): Platform = AndroidPlatform()