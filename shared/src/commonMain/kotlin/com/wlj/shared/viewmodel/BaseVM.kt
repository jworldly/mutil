package com.wlj.shared.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wlj.shared.net.loading.LoadingManager
import com.wlj.shared.tools
import com.wlj.shared.utils.WhileUiSubscribed
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
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

    //公用属性
    var msg: String? = null
    var painter: Painter? = null
    var block: ((Boolean) -> Unit)? = null // Boolean:为重试时是否silent

    //loading专用，不写到构造里时为了单例
    var time: Long? = null  //Loading状态的超时时间
    var silent: Boolean = false // true：点重试，刷新时，需要静默
    object Loading : CommonState()

    object TimeOut : CommonState()
    object Empty : CommonState()
    object Content : CommonState()
    data class Error(var e: Throwable? = null) : CommonState()

}


/**
 * ui响应的事件
 *
 * Effect 指Android中的一次性事件，比如toast、navigation、backpress、click等等，由于这些状态都是一次性的消费所
 * 以不能使用livedata和StateFlow，我们可以使用SharedFlow或者Channel，考虑多个Composable中要共享viewmodel获取
 * sideEffect，这里使用SharedFlow更方便。
 */
interface Effect

/**
 *
 * abstract BaseViewModel, viewModel继承与此类，需要定义Intent\State\Effect, 可约束和简化 View -> Intent -> ViewModel -> State\Effect -> View
 *
 * [I: Intent] Action 为了防止和Android的Intent()混淆
 * [M: State\Effect] 用于描述View的显示数据和状态, Effect用于描述SnackBar Toast Navigation 这些（热）事件
 *
 * [state] 需要默认初始化使用ShareFlow默认状态为null和LiveData一致通过下面方法转换
 * ``` kotlin
 * val stateFlow by lazy { _state.stateIn(viewModelScope, WhileSubscribed(), initial) }
 * ```
 * uiState聚合页面的全部UI状态的LiveData
 */
@Suppress("unused")
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
// </editor-fold >

    // <editor-fold desc="state">

    /**继承BaseViewModel需要实现state默认值*/
//    abstract fun initialState(): S

    /**
     * StateFlow
     *
     * 1、初始值要求：必须提供初始值
     * 2、值更新机制：没次更新值，用equals比较，不同才更新(自动去重)
     * 3、重放策略： 隐式等价于replay=1的SharedFlow，但强制保留最新值，新订阅者会立即收到最新值
     *
     * SharedFlow
     *
     * 1、初始值要求：不需要初始值
     * 2、值更新机制：更新值直接发送
     * 3、重放策略：通过replay参数显式控制重放次数
     */
    private val _state by lazy { MutableStateFlow<S?>(value = null) }

    /**
     * asStateFlow
     *
     * 1、零成本转换：仅类型转换，不创建新流
     * 2、状态生命周期与原始 MutableStateFlow 完全同步
     * 3、适合场景：当需要简单暴露 MutableStateFlow 的只读视图时
     *
     * stateIn
     *
     * 1、创建新的 StateFlow 实例。
     * 2、控制订阅者的激活条件（通过 started 参数）
     * 3、适合场景：当需要统一管理流的生命周期（如根据 UI 订阅状态自动启停）
     */
    val state by lazy { _state.stateIn(viewModelScope, WhileUiSubscribed, replayState) }
//    val state: StateFlow<S> by lazy { _state.asStateFlow() } //


//    //这个是没有初始值的方法
//    protected val _state = MutableSharedFlow<S>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
//
//    val state by lazy { _state.distinctUntilChanged() }

    protected fun emitState(builder: suspend () -> S?) = viewModelScope.launch {
        builder()?.let { _state.emit(it) }
    }

    /**suspend 函数在flow或者scope中emit状态*/
    suspend fun emitState(state: S) = _state.emit(state)

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
    private val _commonState =
        MutableSharedFlow<CommonState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val commonState by lazy {
        _commonState.stateIn(viewModelScope, WhileUiSubscribed, CommonState.Loading)
    }

    fun emitCommonState(builder: suspend () -> CommonState?) = viewModelScope.launch {
        builder()?.let { _commonState.emit(it) }
    }

    suspend fun emitCommonState(state: CommonState) {
        tools.log("发送CommonState：$state")
        _commonState.emit(state)
    }

    open fun refresh(state: CommonState, any: Any? = null) {

    }
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
