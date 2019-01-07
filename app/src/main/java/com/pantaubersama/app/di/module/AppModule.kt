package com.pantaubersama.app.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author edityomurti on 14/12/2018 14:51
 */
@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideAppContext(): Context = application.applicationContext
}