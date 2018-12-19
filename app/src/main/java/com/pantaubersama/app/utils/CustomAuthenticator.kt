//package com.pantaubersama.app.utils
//
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.Retrofit
//import android.content.SharedPreferences
//import com.pantaubersama.app.BuildConfig
//import com.pantaubersama.app.data.api.PantauAPIFactory
//import io.reactivex.disposables.Disposable
//import okhttp3.* //ktlint-disable
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import timber.log.Timber
//import java.io.IOException
//
///**
// * Created by ali on 21/12/17.
// */
//
//class CustomAuthenticator(private val sharedPreferences: SharedPreferences, private val rxSchedulers: RxSchedulers) : Authenticator {
//    private var retrofit: Retrofit? = null
//    private var services: PantauAPIFactory? = null
//
//    @Throws(IOException::class)
//    override fun authenticate(route: Route, response: Response): Request? {
//        if (response.request().header(PantauConstants.Networking.AUTHORIZATION) != null) {
//            return response.request()
//        } else {
//            val client = OkHttpClient.Builder()
//                .build()
//
//            retrofit = Retrofit.Builder()
//                .baseUrl(BuildConfig.PANTAU_BASE_URL)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//
//            services = retrofit?.create<PantauAPIFactory>(PantauAPIFactory::class.java)
//            val refreshTokenResult = services?.refreshToken(PantauConstants.Networking.GRANT_TYPE,
//                BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
//                sharedPreferences.getString(PantauConstants.Networking.REFRESH_TOKEN_FIELD, ""))
//
//            var refreshTokenDisposable: Disposable? = null
//            refreshTokenDisposable = refreshTokenResult
//                ?.subscribeOn(rxSchedulers.io())
//                ?.observeOn(rxSchedulers.mainThread())
//                ?.subscribe(
//                    {
//                        sharedPreferences.edit().putString(PantauConstants.Networking.ACCESS_TOKEN_FIELD, it.accessToken).apply()
//                        sharedPreferences.edit().putString(PantauConstants.Networking.REFRESH_TOKEN_FIELD, it.refreshToken).apply()
//                        if (!refreshTokenDisposable?.isDisposed!!) {
//                            refreshTokenDisposable?.dispose()
//                        }
//                    },
//                    {
//                        Timber.e("Refresh token")
//                        if (!refreshTokenDisposable?.isDisposed!!) {
//                            refreshTokenDisposable?.dispose()
//                        }
//                    }
//                )
//
//            val originalRequest = response.request()
//            val originalUrl = originalRequest.url()
//
//            val url = originalUrl
//                .newBuilder()
//                .setQueryParameter(PantauConstants.Networking.OAUTH_ACCESS_TOKEN_FIELD, sharedPreferences.getString(PantauConstants.Networking.ACCESS_TOKEN_FIELD, ""))
//                .build()
//
//            return originalRequest
//                .newBuilder()
//                .url(url)
//                .build()
//        }
//    }
//}