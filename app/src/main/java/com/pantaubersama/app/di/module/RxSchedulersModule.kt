package com.pantaubersama.app.di.module

import com.pantaubersama.app.utils.RxSchedulers
import dagger.Module
import dagger.Provides

/**
 * Created by alimustofa on 29/01/18.
 */
@Module
class RxSchedulersModule {
    @Provides
    fun provideRxSchedulers(): RxSchedulers {
        return RxSchedulers()
    }
}