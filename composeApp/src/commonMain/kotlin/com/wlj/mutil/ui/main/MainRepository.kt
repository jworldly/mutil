package com.wlj.mutil.ui.main

import com.wlj.mutil.net.Urls
import com.wlj.shared.net.warpPostFlow
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonPrimitive
import org.koin.core.annotation.Single

interface MainRepository {
    fun audit(): Flow<HttpResponse>
}

@Single(binds = [MainRepository::class])
class MainRepositoryImpl(private val client: HttpClient) : MainRepository {

    override fun audit(): Flow<HttpResponse> {
        return client.warpPostFlow(
            Urls.getVerifyCode,
            "phone" to JsonPrimitive("17602345326")
        )
    }
}
