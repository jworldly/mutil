/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wlj.mutil.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.wlj.mutil.ui.MainArgs.MAIN_TAB_ARG
import com.wlj.mutil.ui.MainArgs.TITLE_ARG
import com.wlj.mutil.ui.MainDestinations.MAIN_ROUTE
import com.wlj.mutil.ui.MainDestinations.START_ROUTE
import com.wlj.shared.tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class MainScreens {
    START_SCREEN,
    LOGIN_SCREEN,
    MAIN_SCREEN,
    HOME_SCREEN,

    ;
}

/**
 * Arguments used in [MainDestinations] routes
 */
object MainArgs {
    const val MAIN_TAB_ARG = "mainTab"
    const val TITLE_ARG = "title"
}

/**
 * Destinations used in the [MainActivity]
 * 传参形式{$Key},取值在navArgument里用key
 */
object MainDestinations {
    val START_ROUTE = MainScreens.START_SCREEN.name
    val LOGIN_ROUTE = MainScreens.LOGIN_SCREEN.name
    val MAIN_ROUTE = "${MainScreens.MAIN_SCREEN.name}?{$MAIN_TAB_ARG}"
    val HOME_ROUTE = "${MainScreens.HOME_SCREEN.name}/{$TITLE_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class MainNavigationActions(val navController: NavHostController) {

    init {
        interceptor()
    }

    fun toLogin() {
        navController.navigate(MainDestinations.LOGIN_ROUTE)
    }

    fun toStart(ivs: Boolean = true) {
        navController.navigate(START_ROUTE) {
            popUpTo(navController.graph.findStartDestination().route ?: MAIN_ROUTE) {
                inclusive = true // 如果为true，则从返回堆栈中弹出所有不匹配的目标，直到找到此目标。
                saveState = false //
            }
            launchSingleTop = true
        }
        navController.graph.setStartDestination(START_ROUTE)
    }

    fun toMain(tabIndex: Int = 0) {
        navController.navigate(
            MainScreens.MAIN_SCREEN.name.let { "$it?$tabIndex" }
        ) {
            //在导航之前弹出到给定的目的地。这会从返回堆栈中弹出所有不匹配的目标，直到找到此目标。
            popUpTo(navController.graph.findStartDestination().route ?: MAIN_ROUTE) {
                inclusive = true // 如果为true，则从返回堆栈中弹出所有不匹配的目标，直到找到此目标。
                saveState = false //
            }
            launchSingleTop = true
            restoreState =
                true //是否应恢复以前由 [saveState] 或 'popUpToSaveState' 属性保存的任何状态。如果之前没有保存任何状态并导航到目标 ID，则此作无效。
        }
        navController.graph.setStartDestination(MAIN_ROUTE)
    }

    fun navigate(router: String) {
        navController.navigate(router)
    }

    fun close() {
        navController.popBackStack()
    }

    //拦截器
    fun interceptor() {
        CoroutineScope(Dispatchers.Main).launch {

            navController.currentBackStack.collect {
                it.forEach { backStackEntry ->
                    tools.log("Route: ${backStackEntry.destination.route}")
                }
            }

        }
    }
}
