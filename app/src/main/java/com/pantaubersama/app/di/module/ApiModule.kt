package com.pantaubersama.app.di.module

import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.api.APIFactory
import com.pantaubersama.app.data.api.PantauAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by ali on 02/10/17.
 */
@Module
class ApiModule {
    @Provides
    fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
//                .authenticator(customAuthenticator)
            .build()
    }

    @Provides
    fun providePantauAPI(httpClient: OkHttpClient): PantauAPI{
        return APIFactory.createRetrofit(BuildConfig.PANTAU_BASE_URL, httpClient).create(PantauAPI::class.java)
    }

    @Provides
    fun provideWordStadiumAPI(httpClient: OkHttpClient): PantauAPI{
        return APIFactory.createRetrofit(BuildConfig.WORD_STADIUM_BASE_URL, httpClient).create(PantauAPI::class.java)
    }
}