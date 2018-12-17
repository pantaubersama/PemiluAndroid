package com.pantaubersama.app.di.module

import android.app.Activity
import com.pantaubersama.app.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by ali on 19/10/17.
 */
@Module(includes = [ApiModule::class,
    ConnectionModule::class,
    NetworkModule::class,
    RxSchedulersModule::class,
    SharedPreferenceModule::class])
class ActivityModule(val activity: Activity?){
    @Provides
    @ActivityScope
    fun provideActivity() = activity
}