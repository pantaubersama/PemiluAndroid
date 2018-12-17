package com.pantaubersama.app.di.module

import com.pantaubersama.app.data.api.RestAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by ali on 02/10/17.
 */
@Module
class ApiModule{
    @Provides
    fun provideApi(retrofit: Retrofit) = retrofit.create(RestAPI::class.java)
}