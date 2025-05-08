package com.wlj.mutil.ui.login

import androidx.lifecycle.viewModelScope
import com.wlj.mutil.net.BaseBeanImpl
import com.wlj.shared.kv
import com.wlj.shared.net.warpResponse
import com.wlj.shared.tools
import com.wlj.shared.utils.decodeBean
import com.wlj.shared.utils.decodeBeanFlow
import com.wlj.shared.utils.encodeBean
import com.wlj.shared.viewmodel.Action
import com.wlj.shared.viewmodel.BaseVM
import com.wlj.shared.viewmodel.Effect
import com.wlj.shared.viewmodel.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            kv.decodeBeanFlow("loginbean", LoginBean()).collect {
                tools.log("loginbean decodeValueFlow:${it}")
            }
        }
    }

    override fun onAction(action: LoginAction, currentState: LoginState?) {
        when (action) {
            LoginAction.Google -> tools.toast("google login")
            LoginAction.OnLoginClicked -> login()
            LoginAction.VerifyCode -> getVerifyCode()
        }
    }

    private fun getVerifyCode() {
        repo.fetchVerifyCode("17602345326")
            .warpResponse<BaseBeanImpl<Unit?>>(loading)
            .onEach { result ->
                val s = kv.getString("token", "")
                tools.log("token:$s")
                tools.log("loginbean:${kv.decodeBean("loginbean", LoginBean())}")
            }.launchIn(viewModelScope)
    }

    private fun login() {
        repo.postLogin("17602345326", "666666")
            .warpResponse<BaseBeanImpl<LoginBean>>(loading)
            .onEach { result ->
                result.dataInfo().let {
                    kv.putString("token", it.auth ?: "")
                    kv.encodeBean("loginbean", it)
                }
                emitEffect(LoginEffect.NavigationToHost())
            }.launchIn(viewModelScope)
    }

}

sealed class LoginEffect : Effect {
    data class NavigationToHost(val tab: Int = 0) : LoginEffect()

}

object LoginState : State

sealed class LoginAction : Action {
    object Google : LoginAction()
    object OnLoginClicked : LoginAction()
    object VerifyCode : LoginAction()
}
