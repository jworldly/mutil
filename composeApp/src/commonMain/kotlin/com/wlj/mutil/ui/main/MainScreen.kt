package com.wlj.mutil.ui.main

import MainBottomBar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wlj.mutil.StateEffectScaffold
import com.wlj.mutil.ui.MainNavigationActions
import com.wlj.shared.state.StatePageManager
import com.wlj.shared.tools
import com.wlj.shared.viewmodel.CommonState
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.back_button
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    initialTabIndex: Int = 0,
    navigationActions: MainNavigationActions,
    viewModel: MainVM = koinViewModel(),
    onTabChanged: (Int) -> Unit,
) {
    tools.log("MainScreen 重组")

    LaunchedEffect(initialTabIndex) {
        tools.log("MainScreen initialTabIndex:$initialTabIndex")
        viewModel.sendAction(MainAction.ChangeTab(initialTabIndex))
    }

    val manage = koinInject<StatePageManager>()
        .onRefresh { viewModel.sendAction(MainAction.Refresh) }

    StateEffectScaffold(viewModel, statePageManager = manage) { vm, state1 ->
        tools.log("state1 111$state1")
        Content(state1, navigationActions) {
            onTabChanged(it)
            viewModel.sendAction(MainAction.ChangeTab(it))
        }

    }

}

@Composable
private fun Content(
    state: MainState,
    navigationActions: MainNavigationActions,
    onTabChanged: (Int) -> Unit
) {
    tools.log("Content 重组")
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()// Scaffold底部导航栏
            .border(2.dp, Color.Red),
        bottomBar = { MainBottomBar(state.tabIndex) { onTabChanged(it) } }
    ) { innerPadding ->

        tools.log("TabSwitcher 重组 $innerPadding")

        //nav Height,需要延迟获取
        val bottomPadding by remember(innerPadding) {
            derivedStateOf { innerPadding.calculateBottomPadding() }
        }

        val pagerState = rememberPagerState { 2 }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            key = { it },
            modifier = Modifier.padding(bottom = bottomPadding).border(4.dp, Color.Cyan),
        ) { page ->
            when (page) {
                0 -> HomeScreen(navigationActions)
                1 -> MeScreen(navigationActions)
            }
        }

        LaunchedEffect(state) {
            pagerState.scrollToPage(state.tabIndex)
        }
    }
}

//private val DpSaver = listSaver<Dp, Any>(
//    save = { listOf(it.value) },
//    restore = { Dp(it[0] as Float) }
//)

@Stable
class StableStateWrapper(val value: MainState)


/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentScreen) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
            }
        }
    )
}
