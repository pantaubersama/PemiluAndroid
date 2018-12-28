package com.pantaubersama.app.utils

import com.pantaubersama.app.data.local.cache.DataCache
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor(private val dataCache: DataCache) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(PantauConstants.Networking.OAUTH_ACCESS_TOKEN_FIELD, dataCache.loadToken())
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}