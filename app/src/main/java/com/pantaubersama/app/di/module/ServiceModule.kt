package com.pantaubersama.app.di.module

import android.content.Context
import com.pantaubersama.app.di.scope.ServiceScope
import dagger.Module
import dagger.Provides

/**
 * Created by ali on 19/10/17.
 */
@Module(includes = [ApiModule::class,
    ConnectionModule::class,
    RxSchedulersModule::class,
    SharedPreferenceModule::class])
class ServiceModule(val context: Context?) {
    @Provides
    @ServiceScope
    fun provideContext() = context
}