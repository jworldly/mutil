package com.wlj.mutil.ui.login

import androidx.lifecycle.viewModelScope
import com.wlj.shared.Config
import com.wlj.shared.net.loading.LoadingManager
import com.wlj.shared.tools
import com.wlj.shared.utils.showLoadingDialog
import com.wlj.shared.viewmodel.Action
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.Effect
import com.wlj.shared.viewmodel.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * @Author: wlj
 * @Date: 2025/4/11
 * @Description:
 */

class LoginVM(
    private val repo: LoginRepository,
) : BaseVM<LoginAction, LoginState, LoginEffect>() {

    override fun initialState(): LoginState {
        return LoginState.Loading
    }

    override fun onAction(action: LoginAction, currentState: LoginState?) {
        when (action) {
            LoginAction.OnLoginClicked -> login()
            LoginAction.getVerifyCode -> getVerifyCode()
            else -> {}
        }
    }

    private fun getVerifyCode() {
        repo.fetchVerifyCode("17602345326")
            .showLoadingDialog(loading)
            .onEach { result ->
                showToast("获取验证码成功")
            }.launchIn(viewModelScope)
    }

    private fun login() {
        repo.postLogin("17602345326", "666666")
            .onStart {
                emitState(LoginState.Loading)
            }.catch { ex ->
                Config.errorHandler.onError(ex)
                tools.log(" catch1")
            }.catch { ex ->
                tools.log(" catch2")
            }
            .onEach { result ->
                showToast("登录成功")
                emitEffect(LoginEffect.NavigationToHost(0))
            }.launchIn(viewModelScope)
    }

}

sealed class LoginEffect : Effect {
    data class NavigationToHost(val response: Int) : LoginEffect()

}

sealed class LoginState : State {
    data object Loading : LoginState()
    data class Error(val ex: Throwable) : LoginState()
}

sealed class LoginAction : Action {
    object OnLoginClicked : LoginAction()
    object getVerifyCode : LoginAction()
}
