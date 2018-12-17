package com.pantaubersama.app.di.module

import android.content.SharedPreferences
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.utils.CustomAuthenticator
import com.pantaubersama.app.utils.RxSchedulers
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by ali on 02/10/17.
 */
@Module(includes = [(AppModule::class)])
class NetworkModule {

    @Provides
    fun provideAuthenticator(sharedPreferences: SharedPreferences, rxSchedulers: RxSchedulers): CustomAuthenticator {
        return CustomAuthenticator(sharedPreferences, rxSchedulers)
    }

//    @Provides
//    fun provideloggingInterceptor(): HttpLoggingInterceptor{
//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//        return logging
//    }

    @Provides
    fun httpClient(customAuthenticator: CustomAuthenticator): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .authenticator(customAuthenticator)
                .build()
    }

    @Provides
    fun provideCall(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.PANTAU_BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}