package com.pantaubersama.app.di.module

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.data.remote.PantauAPI
import com.pantaubersama.app.data.remote.PantauOAuthAPI
import com.pantaubersama.app.data.remote.WordStadiumAPI
import com.pantaubersama.app.utils.ConnectionState
import com.pantaubersama.app.utils.CustomAuthenticator
import com.pantaubersama.app.utils.RequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Created by ali on 02/10/17.
 */
@Module(includes = [SharedPreferenceModule::class, ConnectionModule::class])
class ApiModule {
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun provideNetworkInterceptor(dataCache: DataCache): RequestInterceptor {
        return RequestInterceptor(dataCache)
    }

    @Provides
    fun provideCustomAuthenticator(dataCache: DataCache, loggingInterceptor: HttpLoggingInterceptor): CustomAuthenticator {
        return CustomAuthenticator(dataCache, loggingInterceptor)
    }

    @Provides
    fun httpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: RequestInterceptor,
        customAuthenticator: CustomAuthenticator,
        connectionState: ConnectionState): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(requestInterceptor)
//            .addInterceptor { chain ->
//                if (!connectionState.isConnected()) {
//                    throw Exception("Tidak terhubung internet")
//                }
//
//                val response = chain.proceed(chain.request())
//
//                try {
//                    if (!response.isSuccessful) {
//                        throw Exception("Koneksi bermasalah : " + response.body()?.string())
//                    }
//                } catch (e: Exception) {
//                    throw Exception("Koneksi bermasalah : " + response.body()?.string())
////                    e.printStackTrace()
//                }
//
//
//                response
//            }
            .authenticator(customAuthenticator)
            .build()
    }

    @Provides
    fun providePantauAPI(httpClient: OkHttpClient): PantauAPI {
        return APIWrapper.createRetrofit(BuildConfig.PANTAU_BASE_URL, httpClient).create(PantauAPI::class.java)
    }

    @Provides
    fun providePantauOAuthAPI(httpClient: OkHttpClient): PantauOAuthAPI {
        return APIWrapper.createRetrofit(BuildConfig.PANTAU_OAUTH_URL, httpClient).create(PantauOAuthAPI::class.java)
    }

    @Provides
    fun provideWordStadiumAPI(httpClient: OkHttpClient): WordStadiumAPI {
        return APIWrapper.createRetrofit(BuildConfig.WORD_STADIUM_BASE_URL, httpClient).create(WordStadiumAPI::class.java)
    }
}