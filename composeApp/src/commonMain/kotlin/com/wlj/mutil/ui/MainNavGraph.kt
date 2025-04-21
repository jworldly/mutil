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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wlj.mutil.ui.MainArgs.MAIN_TAB_ARG
import com.wlj.mutil.ui.login.LoginScreen
import com.wlj.mutil.ui.main.MainScreen
import com.wlj.mutil.ui.start.StartScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController = rememberNavController(),// nav控制
//    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = MainDestinations.START_ROUTE,//默认屏
    navActions: MainNavigationActions = remember(navController) { //nav参数封装
        MainNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(MainDestinations.START_ROUTE) {
            StartScreen(modifier,navActions) { navActions.toMain() }
        }
        // 登录
        composable(MainDestinations.LOGIN_ROUTE) {
            LoginScreen(modifier,navActions) { navActions.close() }
        }
        // 主屏
        composable(
            MainDestinations.MAIN_ROUTE, arguments = listOf(
                navArgument(MAIN_TAB_ARG) { type = NavType.IntType; defaultValue = 0 }
            )) { entry ->

            MainScreen(
                entry.arguments?.getInt(MAIN_TAB_ARG)!!,
                navActions
            ) { entry.arguments?.putInt(MAIN_TAB_ARG, it) }
        }
    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = 1
const val DELETE_RESULT_OK = 2
const val EDIT_RESULT_OK = 3
