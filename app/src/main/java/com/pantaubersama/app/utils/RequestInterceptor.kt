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
        var url = originalHttpUrl.newBuilder().build()
        val stringEndpoint = originalHttpUrl.toString()

        if (dataCache.loadLoginState()) {
            for (i in PantauConstants.Networking.NON_TOKEN_URL.indices) {
                if (!stringEndpoint.contains(PantauConstants.Networking.NON_TOKEN_URL[i])) {
                    if (dataCache.loadToken() != null) {
                        url =
                            originalHttpUrl.newBuilder()
                                .addQueryParameter(PantauConstants.Networking.OAUTH_ACCESS_TOKEN_FIELD, dataCache.loadToken())
                                .build()
                    }
                }
            }
        }

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}