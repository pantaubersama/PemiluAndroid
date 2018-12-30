package com.pantaubersama.app.utils

import com.pantaubersama.app.data.local.cache.DataCache
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor(private val dataCache: DataCache) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()

        if (dataCache.loadToken() != null) {
            req.addHeader(PantauConstants.Networking.AUTHORIZATION, dataCache.loadToken()!!)
        }

        val requestBuilder = req.build()

        return chain.proceed(requestBuilder)
    }
}