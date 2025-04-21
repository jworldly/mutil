package com.wlj.mutil.ui.main

import MainBottomBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import com.wlj.mutil.StateEffectScaffold
import com.wlj.mutil.ui.MainDestinations
import com.wlj.mutil.ui.MainNavigationActions
import com.wlj.shared.tools
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.back_button
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MainScreen(
    initialTabIndex: Int = 0,
    navigationActions: MainNavigationActions,
    viewModel: MainVM = koinViewModel(),
    onTabChanged: (Int) -> Unit,
) {

    LaunchedEffect(initialTabIndex) {
        tools.log("MainScreen initialTabIndex:$initialTabIndex")
        viewModel.sendAction(MainAction.ChangeTab(initialTabIndex))
    }

    StateEffectScaffold(
        viewModel,
    ) { vm, state ->

        content(
            state,
            onTabChanged = {
                onTabChanged(it)
                vm.sendAction(MainAction.ChangeTab(it))
            },
            navigationActions
        )
    }

}

@Composable
private fun content(
    state: MainState,
    onTabChanged: (Int) -> Unit,
    navigationActions: MainNavigationActions
) {
    tools.log("MainScreen s:$state")

    // 主内容
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {},
        bottomBar = {
            MainBottomBar(
                selectedTabIndex = when (state) {
                    is MainState.Tab -> state.index
                },
                onTabClick = {
                    onTabChanged(it)
                },
            )
        }
    ) { innerPadding ->
        tools.log("MainScreen innerPadding:$innerPadding")
        val modifier = Modifier.padding(innerPadding)
        when (state) {
            is MainState.Tab -> {
                when (state.index) {
                    0 -> HomeScreen(modifier = modifier) { navigationActions.toStart() }
                    1 -> MeScreen(modifier = modifier){ navigationActions.toLogin()}
                }
            }

        }
    }
}

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
