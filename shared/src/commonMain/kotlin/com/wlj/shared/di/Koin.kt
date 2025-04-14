package com.wlj.shared.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * @Author: wlj
 * @Date: 2025/4/11
 * @Description:
 */
class Koin {


}

/**
 * 初始化koin
 */
fun initKoin(appModule: Module): KoinApplication {

    return startKoin {
        modules(
            listOf(
                appModule,
            )
        )
    }
}