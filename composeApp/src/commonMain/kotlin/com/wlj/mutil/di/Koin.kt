package com.wlj.mutil.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module

/**
 * @Author: wlj
 * @Date: 2025/4/14
 * @Description:
 */

/**
 * [Koin注释文档](https://insert-koin.io/docs/reference/koin-annotations/start)
 */
@Module
@ComponentScan("com.wlj.mutil.ui")
class UiModule

val AppModule = arrayOf(
    UiModule().module //用注解生成的

)