package com.wlj.mutil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.wlj.shared.state.StatePageManager
import com.wlj.shared.state.Status
import com.wlj.shared.tools
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.CommonState.Content
import com.wlj.shared.viewmodel.CommonState.Empty
import com.wlj.shared.viewmodel.CommonState.Error
import com.wlj.shared.viewmodel.CommonState.Loading
import com.wlj.shared.viewmodel.CommonState.TimeOut
import com.wlj.shared.viewmodel.Effect
import com.wlj.shared.viewmodel.State
import kotlinx.coroutines.withContext
import kotlin.apply
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 *
 * @param initialState content内容页的初始状态
 * @param statePageManager  错误、空、加载、内容 状态页，要启用就传值就启用
 * @param sideEffect  附加效应，传值就启用
 * @param content  页面内容
 */
@Composable
fun <S : State, E : Effect, VM : BaseVM<*, S, E>> StateEffectScaffold(
    viewModel: VM,
    initialState: S? = null,
    statePageManager: StatePageManager? = null,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    sideEffect: (suspend (VM, E) -> Unit)? = null,
    content: (@Composable (VM, S) -> Unit)? = null//{_, _ -> }
) {
    tools.log("StateEffectScaffold 重组触发")

    // 附加效应
    sideEffect?.let {
        val lambdaEffect by rememberUpdatedState(sideEffect)
        LaunchedEffect(viewModel.effect, lifecycle, minActiveState) {
            /**
             *  repeatOnLifecycle:当minActiveState时才会开始收集，重新minActiveState时重新收集,适合多个状态的collect。
             *  官方推荐：使用repeatOnLifecycle在界面层收集Flow。调用repeatOnLifecycle的协程将不会继续执行后面的代码了，
             *  当它恢复的时候，已经是ui DESTROY的时候了，所以不要在repeatOnLifecycle的后面继续repeatOnLifecycle。
             *  官方推荐在repeatOnLifecycle里面launch多次，开启多个协程，然后在里面collect，相互不影响。
             *
             *  flowWithLifecycle:  如果只有一个Flow数据需要收集，那么官方推荐使用flowWithLifecycle。
             */
            lifecycle.repeatOnLifecycle(minActiveState) {
                if (context == EmptyCoroutineContext) {
                    viewModel.effect.collect { lambdaEffect(viewModel, it) }
                } else withContext(context) {
                    viewModel.effect.collect { lambdaEffect(viewModel, it) }
                }
            }
        }
    }
    // 页面状态
    @Composable
    fun uiStateFun() {
        content ?: return
        val uiState by viewModel.state.collectAsStateWithLifecycle(
            initialValue = viewModel.replayState,
            lifecycle = lifecycle,
            minActiveState = minActiveState,
            context = context
        )
        (uiState ?: initialState)?.let { content.invoke(viewModel, it) }
    }

    //错误、空、加载、内容 状态页，要启用就传的初始statePage
    statePageManager?.let {

        StatePage(viewModel, it, lifecycle, minActiveState, context) {
            uiStateFun()
        }
    } ?: uiStateFun()

}


@Composable
fun StatePage(
    vm: BaseVM<*, *, *>,
    manager: StatePageManager,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    uiState: @Composable () -> Unit
) {
    val cuiState by vm.commonState.collectAsStateWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState,
        context = context
    )
    tools.log("StatePage 收到状态$cuiState")

    when (cuiState) {
        is Loading -> {
            //超时-> TimeOut状态
            val state = cuiState as Loading
            state.block = { { vm.emitCommonState { TimeOut } } }
            manager.showLoading(Status.LOADING, cuiState,state.silent)
        }

        is TimeOut -> {
            //重试 切换到Loading
            (cuiState as TimeOut).block = { vm.emitCommonState { Loading.apply { silent = it } } }
            manager.showError(Status.ERROR, cuiState)
        }

        is Empty -> {
            //重试 切换到Loading
            (cuiState as Empty).block = { vm.emitCommonState { Loading.apply { silent = it } } }
            manager.showEmpty(Status.EMPTY, cuiState)
        }

        is Error -> {
            //重试 切换到Loading
            (cuiState as Error).block = { vm.emitCommonState { Loading.apply { silent = it } } }
            manager.showError(Status.ERROR, cuiState)
        }

        is Content -> {
            manager.showContent(Status.CONTENT, cuiState)
            uiState()
        }
    }

}

