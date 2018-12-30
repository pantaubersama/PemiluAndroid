package com.pantaubersama.app.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.data.remote.PantauAPI
import com.pantaubersama.app.data.remote.PantauOAuthAPI
import com.pantaubersama.app.data.remote.WordStadiumAPI
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
@Module(includes = [SharedPreferenceModule::class])
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

//    private fun isNetworkAvailable(context: Context): Boolean {
//        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        var activeNetworkInfo = connectivityManager.activeNetworkInfo
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected
//    }

    @Provides
    fun httpClient(loggingInterceptor: HttpLoggingInterceptor, requestInterceptor: RequestInterceptor, customAuthenticator: CustomAuthenticator): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(requestInterceptor)
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