package com.pantaubersama.app.utils

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.remote.PantauOAuthAPI
import okhttp3.* // ktlint-disable
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException

class CustomAuthenticator(
    private val dataCache: DataCache,
    private val loggingInterceptor: HttpLoggingInterceptor
) : Authenticator {
    private var retrofit: Retrofit? = null
    private var oAuthApi: PantauOAuthAPI? = null

    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request? {
        synchronized(this) {
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.PANTAU_OAUTH_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            oAuthApi = retrofit?.create<PantauOAuthAPI>(PantauOAuthAPI::class.java)

            val refreshToken = oAuthApi?.refreshToken(
                PantauConstants.Networking.GRANT_TYPE,
                BuildConfig.PANTAU_CLIENT_ID,
                BuildConfig.PANTAU_CLIENT_SECRET,
                dataCache.loadRefreshToken())?.execute()

            if (refreshToken?.code() == 200) {
                dataCache.saveToken(refreshToken.body()?.accessToken!!)
                dataCache.saveRefreshToken(refreshToken.body()?.refreshToken!!)
                val originalRequest = response.request()
                val originalUrl = originalRequest.url()

                val url = originalUrl
                    .newBuilder()
                    .build()

                return originalRequest
                    .newBuilder()
                    .removeHeader(PantauConstants.Networking.AUTHORIZATION)
                    .addHeader(PantauConstants.Networking.AUTHORIZATION, dataCache.loadToken()!!)
                    .url(url)
                    .build()
            } else {
                Timber.d("FAILED REFRESHING TOKEN –– caused by : ${refreshToken?.message()}")
                return null
            }
        }
    }
}