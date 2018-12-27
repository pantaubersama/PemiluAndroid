package com.pantaubersama.app.utils

import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.remote.PantauOAuthAPI
import io.reactivex.disposables.Disposable
import okhttp3.* // ktlint-disable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException

class CustomAuthenticator(
    private val dataCache: DataCache,
    private val rxSchedulers: RxSchedulers
) : Authenticator {
    private var retrofit: Retrofit? = null
    private var oAuthApi: PantauOAuthAPI? = null

    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request? {
        if (response.request().header(PantauConstants.Networking.AUTHORIZATION) != null) {
            return response.request()
        } else {
            val client = OkHttpClient.Builder()
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.PANTAU_OAUTH_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            oAuthApi = retrofit?.create<PantauOAuthAPI>(PantauOAuthAPI::class.java)

            var refreshToken: Disposable? = null
            refreshToken = oAuthApi?.refreshToken(
                PantauConstants.Networking.GRANT_TYPE,
                BuildConfig.PANTAU_CLIENT_ID,
                BuildConfig.PANTAU_CLIENT_SECRET,
                dataCache.loadRefreshToken())
                ?.subscribeOn(rxSchedulers.io())
                ?.observeOn(rxSchedulers.mainThread())
                ?.subscribe(
                    {
                        dataCache.saveToken(it.token?.accessToken!!)
                        dataCache.saveRefreshToken(it.token?.refreshToken!!)
                        if (!refreshToken?.isDisposed!!) {
                            refreshToken?.dispose()
                        }
                    }, {
                        Timber.e(it)
                        if (!refreshToken?.isDisposed!!) {
                            refreshToken?.dispose()
                        }
                    }
                )

            val originalRequest = response.request()
            val originalUrl = originalRequest.url()

            val url = originalUrl
                    .newBuilder()
                    .setQueryParameter(PantauConstants.Networking.OAUTH_ACCESS_TOKEN_FIELD, dataCache.loadToken())
                    .build()

            return originalRequest
                    .newBuilder()
                    .url(url)
                    .build()
        }
    }
}