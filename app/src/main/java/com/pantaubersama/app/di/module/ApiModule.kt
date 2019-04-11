package com.pantaubersama.app.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.remote.* // ktlint-disable
import com.pantaubersama.app.utils.ConnectionState
import com.pantaubersama.app.utils.CustomAuthenticator
import com.pantaubersama.app.utils.NetworkErrorInterceptor
import com.pantaubersama.app.utils.RequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by ali on 02/10/17.
 */
@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideNetworkErrorInterceptor(connectionState: ConnectionState): NetworkErrorInterceptor {
        return NetworkErrorInterceptor(connectionState)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
            HttpLoggingInterceptor.Level.NONE
        return logging
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(dataCache: DataCache): RequestInterceptor {
        return RequestInterceptor(dataCache)
    }

    @Provides
    @Singleton
    fun provideCustomAuthenticator(dataCache: DataCache, loggingInterceptor: HttpLoggingInterceptor, networkErrorInterceptor: NetworkErrorInterceptor): CustomAuthenticator {
        return CustomAuthenticator(dataCache, loggingInterceptor, networkErrorInterceptor)
    }

    @Provides
    @Singleton
    fun httpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: RequestInterceptor,
        customAuthenticator: CustomAuthenticator,
        networkErrorInterceptor: NetworkErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(requestInterceptor)
            .addInterceptor(networkErrorInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(customAuthenticator)
            .build()
    }

    @Provides
    @Named("OpiniumService")
    @Singleton
    fun opiniumServiceHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkErrorInterceptor: NetworkErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("ApiKey", "Bearer " + BuildConfig.OPINIUM_SERVICE_API_KEY).build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(networkErrorInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providePantauAPI(httpClient: OkHttpClient): PantauAPI {
        return APIWrapper.createRetrofit(BuildConfig.PANTAU_BASE_URL, httpClient).create(PantauAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePantauOAuthAPI(httpClient: OkHttpClient): PantauOAuthAPI {
        return APIWrapper.createRetrofit(BuildConfig.PANTAU_OAUTH_URL, httpClient).create(PantauOAuthAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideWordStadiumAPI(httpClient: OkHttpClient): WordStadiumAPI {
        return APIWrapper.createRetrofit(BuildConfig.WORD_STADIUM_BASE_URL, httpClient).create(WordStadiumAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideOEmbedAPI(httpClient: OkHttpClient): OEmbedApi {
        return APIWrapper.createRetrofit("https://publish.twitter.com/", httpClient).create(OEmbedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOpiniumServiceAPI(@Named("OpiniumService") opiniumServiceHttpClient: OkHttpClient): OpiniumServiceAPI {
        return APIWrapper.createRetrofit(BuildConfig.OPINIUM_SERVICE_BASE_URL, opiniumServiceHttpClient).create(OpiniumServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationApi(httpClient: OkHttpClient): NotificationApi {
        return APIWrapper.createRetrofit(BuildConfig.NOTIFICATION_BASE_URL, httpClient).create(NotificationApi::class.java)
    }
}