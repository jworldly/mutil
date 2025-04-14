package com.wlj.mutil.login

import androidx.lifecycle.viewModelScope
import com.wlj.shared.viewmodel.Action
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.Effect
import com.wlj.shared.viewmodel.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * @Author: wlj
 * @Date: 2025/4/11
 * @Description:
 */
interface LoginRepo {
    fun fetchLogin(): Flow<Int>
}

class LoginVM(
    private val repo: LoginRepo
) : BaseVM<LoginAction, LoginState, LoginEffect>() {

    override fun initialState(): LoginState {
        return LoginState.Loading
    }

    override fun onAction(action: LoginAction, currentState: LoginState?) {
        when (action) {
            LoginAction.OnLoginClicked -> login()
            else -> {}
        }
    }


    private fun login() {
        repo.fetchLogin()
            .onStart {
                emitState(LoginState.Loading)
            }.catch { ex ->
                emitState(LoginState.Error(ex))
            }.onEach { result ->
                showToast("登录成功")
                emitEffect(LoginEffect.NavigationToHost(result))
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
}
