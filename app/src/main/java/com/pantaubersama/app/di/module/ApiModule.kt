package com.pantaubersama.app.di.module

import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.data.remote.PantauAPI
import com.pantaubersama.app.data.remote.PantauOAuthAPI
import com.pantaubersama.app.data.remote.WordStadiumAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by ali on 02/10/17.
 */
@Module
class ApiModule {
    @Provides
    fun provideloggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun httpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun providePantauAPI(httpClient: OkHttpClient): PantauAPI {
        Timber.d("TESTS providePantauAPI base = ${BuildConfig.PANTAU_BASE_URL}")
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