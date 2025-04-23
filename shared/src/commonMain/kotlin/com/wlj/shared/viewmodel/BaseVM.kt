package com.wlj.shared.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wlj.shared.net.loading.LoadingManager
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * 用户与ui的交互事件
 * action用于描述各种请求State或者Effect的动作，由View发送ViewModel订阅消费，典型的生产者消费者模式，考虑是一对一
 * 的关系我们使用Channel来实现，有些开发者喜欢直接调用ViewModel方法，如果方法还有返回值，就破坏了数据的单向流动。
 */
interface Action

/**
 * ui响应的状态
 *
 * State需要订阅观察者模式给view提供数据，在非Compose中我们可以使用LiveData和StateFlow, 在Compose中我们可以直接
 * 使用State。为了兼容性我们选择StateFlow或者自定义SharedFlow。
 */
interface State

// 通用效果密封类（独立定义）
sealed class CommonState : State {
    data class Loading(val message: String? = null) : CommonState()
    data class Empty(val message: String? = null, val retry: (() -> Unit)? = null) : CommonState()
    data class Error(val message: String? = null, val retry: (() -> Unit)? = null) : CommonState()
}


/**
 * ui响应的事件
 *
 * Effect 指Android中的一次性事件，比如toast、navigation、backpress、click等等，由于这些状态都是一次性的消费所
 * 以不能使用livedata和StateFlow，我们可以使用SharedFlow或者Channel，考虑多个Composable中要共享viewmodel获取
 * sideEffect，这里使用SharedFlow更方便。
 */
interface Effect

// 通用效果密封类（独立定义）
sealed class CommonEffect : Effect {
    data class Toast(val message: String) : CommonEffect()
    // 可扩展其他通用效果：Analytics, Logging 等
}


/**
 *
 * abstract BaseViewModel, viewModel继承与此类，需要定义Intent\State\Effect, 可约束和简化 View -> Intent -> ViewModel -> State\Effect -> View
 *
 * [I: Intent] Action 为了防止和Android的Intent()混淆
 * [M: State\Effect] 用于描述View的显示数据和状态, Effect用于描述SnackBar Toast Navigation 这些（热）事件
 *
 * [stateFlow] 需要默认初始化使用ShareFlow默认状态为null和LiveData一致通过下面方法转换
 * ``` kotlin
 * val stateFlow by lazy { _state.stateIn(viewModelScope, WhileSubscribed(), initial) }
 * ```
 * uiState聚合页面的全部UI状态的LiveData
 */
abstract class BaseVM<A : Action, S : State, E : Effect> : ViewModel(), KoinComponent {

    val loading: LoadingManager by inject()

    // <editor-fold desc="action">
    private val _action = Channel<A>()

    init {
        viewModelScope.launch {
            _action.consumeAsFlow().collect {
                /*replayState：很多时候我们需要通过上个state的数据来处理这次数据，所以我们要获取当前状态传递*/
                onAction(it, replayState)
            }
        }
    }

    /** [actor] 用于在非viewModelScope外使用*/
    val actor: SendChannel<A> by lazy { _action }

    fun sendAction(action: A) = viewModelScope.launch {
        _action.send(action)
    }

    /** 订阅事件的传入 onAction()分发处理事件 */
    protected abstract fun onAction(action: A, currentState: S?)
// </editor-fold desc="state">

// <editor-fold desc="state">
    /**继承BaseViewModel需要实现state默认值*/
    @Suppress("CAST_NEVER_SUCCEEDS")
    protected open fun initialState(): S = null as S
//    private val _state by lazy {
//        MutableStateFlow(value = initialState())
//    }
//    /**在view中用于订阅*/
//    val state: StateFlow<S> by lazy { _state.asStateFlow() }

    //这个是没有初始值的方法
    private val _state =
        MutableSharedFlow<S>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val state: Flow<S> by lazy { _state.distinctUntilChanged() }

    //Flow -> StateFlow,用这个必须实现 initialState（）
    val stateFlow by lazy { _state.stateIn(viewModelScope, WhileSubscribed(), initialState()) }

    protected fun emitState(builder: suspend () -> S?) = viewModelScope.launch {
        builder()?.let { _state.emit(it) }
    }

    /**suspend 函数在flow或者scope中emit状态*/
    protected suspend fun emitState(state: S) = _state.emit(state)

    /** [replayState] 重放当前uiState,replay始终是1 */
    val replayState
        get() = _state.replayCache.firstOrNull()

// </editor-fold desc="state">

// <editor-fold desc="effect">
    /**
     * [effect]事件带来的副作用，通常是一次性事件 例如：弹Toast、导航Fragment等
     */
    private val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> by lazy { _effect.asSharedFlow() }

    protected fun emitEffect(builder: suspend () -> E?) = viewModelScope.launch {
        builder()?.let { _effect.emit(it) }
    }

    protected suspend fun emitEffect(effect: E) = _effect.emit(effect)
// </editor-fold desc="effect">

    // <editor-fold desc=" common">
    // 新增通用效果通道（独立于业务效果）
    private val _commonEffect = MutableSharedFlow<CommonEffect>()
    val commonEffect: SharedFlow<CommonEffect> = _commonEffect.asSharedFlow()

    // 类型安全的 Toast 扩展
    protected fun showToast(msg: String) {
        viewModelScope.launch {
            _commonEffect.emit(CommonEffect.Toast(msg))
        }
    }

    // 新增通用效果通道（独立于业务效果）
    protected val _commonState = MutableSharedFlow<CommonState>()
    val commonState: SharedFlow<CommonState> = _commonState.asSharedFlow()




// </editor-fold desc=" common">

// <editor-fold desc="test">
    /** 不挂起发送 state ，返回 boolean */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun tryEmitState(state: S) = _state.tryEmit(state)

    /**不挂起发送 effect ，返回 ChannelResult */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun tryEmitEffect(effect: E) = _effect.tryEmit(effect)
// </editor-fold desc="test">

}
