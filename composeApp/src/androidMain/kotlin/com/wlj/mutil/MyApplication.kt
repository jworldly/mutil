package com.wlj.mutil

import android.app.Application
import com.wlj.mutil.di.AppModule
import com.wlj.mutil.net.headers
import com.wlj.shared.Config
import com.wlj.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.core.module.Module

/**
 * @Author: wlj
 * @Date: 2025/4/11
 * @Description:
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Config.apply {
            debug = BuildConfig.DEBUG
            headersBuilder.headers()
        }
        //初始化
        initKoin(getAppModule())
            .androidContext(this)
            .androidLogger(Level.INFO)
    }

    private fun getAppModule(): Module {
        return AppModule
    }
}