package com.wlj.mutil.ui.login

import com.wlj.mutil.net.BaseBeanImpl
import com.wlj.mutil.net.Urls
import com.wlj.shared.net.warpPostFlow
import com.wlj.shared.net.warpPostFlowBaseBean
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonPrimitive
import org.koin.core.annotation.Single

/**
 * @Author: wlj
 * @Date: 2025/4/14
 * @Description:
 */
interface LoginRepository {
    fun fetchVerifyCode(phone: String): Flow<BaseBeanImpl<Unit?>>
    fun postLogin(phone: String, code: String): Flow<Unit?>
}

@Single(binds = [LoginRepository::class])
class LoginRepositoryImpl(private val client: HttpClient) : LoginRepository {

    override fun fetchVerifyCode(phone: String): Flow<BaseBeanImpl<Unit?>> {
        return client.warpPostFlow(
            Urls.getVerifyCode,
            "phone" to JsonPrimitive(phone)
        )
    }

    override fun postLogin(phone: String, code: String): Flow<Unit?> {
        return client.warpPostFlowBaseBean<Unit?, BaseBeanImpl<Unit?>>(
            Urls.login,
            "phone" to JsonPrimitive(phone),
            "code" to JsonPrimitive(code),
        )
    }
}

