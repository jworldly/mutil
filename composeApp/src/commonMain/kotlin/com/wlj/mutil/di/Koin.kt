package com.wlj.mutil.di

import com.wlj.mutil.ui.login.LoginRepository
import com.wlj.mutil.ui.login.LoginRepositoryImpl
import com.wlj.mutil.ui.login.LoginVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * @Author: wlj
 * @Date: 2025/4/14
 * @Description:
 */
val AppModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    viewModel { LoginVM(get()) }

}