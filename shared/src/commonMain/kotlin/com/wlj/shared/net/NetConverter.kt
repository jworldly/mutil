package com.wlj.shared.net

import com.wlj.shared.net.exception.RequestParamsException
import com.wlj.shared.net.exception.ServerResponseException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.util.reflect.TypeInfo
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

interface NetConverter {

    @Throws(Throwable::class)
    suspend fun <R> onConvert(k: TypeInfo, response: HttpResponse): R

}

@Suppress("UNCHECKED_CAST")
class JsonConvert() : NetConverter {

    override suspend fun <R> onConvert(k: TypeInfo, response: HttpResponse): R {

        if (!response.status.isSuccess())
            throw ServerResponseException(response, response.status.description)

        // 根据返回的code码进行判断
        return when (k) {
            typeOf<String>(), typeOf<ByteArray>(), typeOf<Unit>() -> response.body(k)

            else -> {
                if(k.type as? KClass<BaseBean<*>> != null){
                    val bean = response.body<BaseBean<*>>(k)
                    return when (bean.dataCode()) {
                        in 200..299 -> bean/*.dataInfo()*/ as R
                        in 400..499 -> throw RequestParamsException(response, bean.dataMsg())
                        else -> throw ServerResponseException(response, bean.dataMsg())
                    }
                } else {
                    throw ServerResponseException(response, "") // 服务器异常错误
                }
            }
        }

    }

}
