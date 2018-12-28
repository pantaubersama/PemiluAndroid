package com.pantaubersama.app.utils

import com.pantaubersama.app.data.local.cache.DataCache
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor(private val dataCache: DataCache) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (dataCache.loadLoginState()) {
            if (dataCache.loadToken() != null) {
                val headers = request
                    .headers()
                    .newBuilder()
                    .add(PantauConstants.Networking.AUTHORIZATION, PantauConstants.Networking.BEARER+dataCache.loadToken()!!)
                    .build()
                request = request
                    .newBuilder()
                    .headers(headers)
                    .build()
            }
        }
        return chain.proceed(request)
    }
}