package com.wlj.mutil.ui.main

import androidx.lifecycle.viewModelScope
import com.wlj.mutil.net.BaseBeanImpl
import com.wlj.shared.net.warpResponseState
import com.wlj.shared.viewmodel.Action
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.CommonState
import com.wlj.shared.viewmodel.Effect
import com.wlj.shared.viewmodel.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel

/**
 * @Author: wlj
 * @Date: 2025/4/20
 * @Description:
 */
@KoinViewModel
class MainVM(private val rep: MainRepository) : BaseVM<MainAction, MainState, MainEffect>() {

    override fun onAction(action: MainAction, currentState: MainState?) {
        val mainState = currentState ?: MainState(0)
        when (action) {
            is MainAction.ChangeTab -> {
                emitState { mainState.copy(tabIndex = action.index) }
            }

            MainAction.Refresh -> refresh(commonState.value)
        }
    }

    /**
     *
     * 1、[Action]添加事件
     * ```
     * object Refresh : MainAction()
     * ```
     *
     * 2、页面发送刷新请求：
     * ```
     *   val manage = StatePageManager().onRefresh { viewModel.sendAction(MainAction.Refresh) }
     * ```
     * 3、onAction接收调用[refresh]
     * ```
     * MainAction.Refresh -> refresh(commonState.value)
     * ```
     *
     */
    override fun refresh(state: CommonState, any: Any?) {
        auditState(true)
    }

   private fun auditState(refresh: Boolean = false) {
        rep.audit()
            .warpResponseState<BaseBeanImpl<Unit?>>(this,refresh)
//            .onEach { result -> }
            .launchIn(viewModelScope)
    }

}

//VM -> 页面 副作用
sealed class MainEffect : Effect {}

//VM -> 页面 State
data class MainState(
    val tabIndex: Int = 0,

) : State

//页面 -> VM
sealed class MainAction : Action {
    object Refresh : MainAction()
    data class ChangeTab(val index: Int) : MainAction()
}

