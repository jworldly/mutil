package com.wlj.mutil.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wlj.shared.viewmodel.Action
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.Effect
import com.wlj.shared.viewmodel.State
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * @Author: wlj
 * @Date: 2025/4/20
 * @Description:
 */
@KoinViewModel
class MainVM() : BaseVM<MainAction, MainState, MainEffect>() {


    override fun onAction(action: MainAction, currentState: MainState?) {
        when (action) {
            is MainAction.ChangeTab -> {
                viewModelScope.launch {
                    emitState(MainState.Tab(action.index))
                }
            }


        }
    }


}

sealed class MainEffect : Effect {

}

sealed class MainState : State {
    data class Tab(val index: Int) : MainState()
}

sealed class MainAction : Action {

    data class ChangeTab(val index: Int) : MainAction()
}

