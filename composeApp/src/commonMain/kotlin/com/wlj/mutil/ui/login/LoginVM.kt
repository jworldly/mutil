package com.wlj.mutil.ui.login

import androidx.lifecycle.viewModelScope
import com.wlj.shared.kv
import com.wlj.shared.net.warpResponse
import com.wlj.shared.tools
import com.wlj.shared.utils.decodeValueFlow
import com.wlj.shared.utils.encodeValue
import com.wlj.shared.utils.handleFlowErrors
import com.wlj.shared.utils.showLoadingDialog
import com.wlj.shared.viewmodel.Action
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.CommonState
import com.wlj.shared.viewmodel.Effect
import com.wlj.shared.viewmodel.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * @Author: wlj
 * @Date: 2025/4/11
 * @Description:
 */

@KoinViewModel
class LoginVM(
    private val repo: LoginRepository,
) : BaseVM<LoginAction, LoginState, LoginEffect>() {

    init {
        viewModelScope.launch {
            kv.decodeValueFlow("loginbean", LoginBean()).collect {
                tools.log("loginbean decodeValueFlow:${it}")
            }
        }
    }

    override fun onAction(action: LoginAction, currentState: LoginState?) {
        when (action) {
            LoginAction.Google -> /*tools.toast("google login")*/ showToast("google")
            LoginAction.OnLoginClicked -> login()
            LoginAction.getVerifyCode -> getVerifyCode()
            else -> {}
        }
    }

    private fun getVerifyCode() {

        repo.fetchVerifyCode("17602345326")
            .warpResponse(loading)
            .onEach { result ->
                val s = kv.getString("token", "")
                tools.log("token:$s")
            }.launchIn(viewModelScope)
    }

    private fun login() {
        repo.postLogin("17602345326", "666666")
            .warpResponse(loading)
            .onEach { result ->
                result?.auth?.let {
                    kv.putString("token", it)
                    kv.encodeValue("loginbean", result)
                }
                emitEffect(LoginEffect.NavigationToHost())
            }.launchIn(viewModelScope)
    }

}

sealed class LoginEffect : Effect {
    data class NavigationToHost(val tab: Int = 0) : LoginEffect()

}

sealed class LoginState : State {
    data object Loading : LoginState()
    data class Error(val ex: Throwable) : LoginState()
}

sealed class LoginAction : Action {
    object Google : LoginAction()
    object OnLoginClicked : LoginAction()
    object getVerifyCode : LoginAction()
}
