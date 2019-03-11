package com.pantaubersama.app.di.module

import com.pantaubersama.app.utils.WordsPNHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author edityomurti on 11/03/2019 13:39
 */
@Module
class WordsModule {
    @Provides
    @Singleton
    fun provideWordsPNHandler(): WordsPNHandler {
        return WordsPNHandler()
    }
}