package com.wlj.mutil.ui.login

import com.wlj.mutil.net.Urls
import com.wlj.shared.net.warpPostFlow
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonPrimitive
import org.koin.core.annotation.Single

/**
 * @Author: wlj
 * @Date: 2025/4/14
 * @Description:登录
 */
interface LoginRepository {
    fun fetchVerifyCode(phone: String): Flow<HttpResponse>
    fun postLogin(phone: String, code: String): Flow<HttpResponse>
}

@Single(binds = [LoginRepository::class])
class LoginRepositoryImpl(private val client: HttpClient) : LoginRepository {

    override fun fetchVerifyCode(phone: String): Flow<HttpResponse> {
        return client.warpPostFlow(
            Urls.getVerifyCode,
            "phone" to JsonPrimitive(phone),
        )
    }

    override fun postLogin(phone: String, code: String): Flow<HttpResponse> {
        return client.warpPostFlow(
            Urls.login,
            "phone" to JsonPrimitive(phone),
            "code" to JsonPrimitive(code),
        )
    }
}