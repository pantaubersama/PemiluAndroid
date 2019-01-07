package com.pantaubersama.app.di.module

import android.app.Activity
import com.pantaubersama.app.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by ali on 19/10/17.
 */
@Module
class ActivityModule(private val activity: Activity) {
    @Provides
    @ActivityScope
    fun provideActivity(): Activity = activity
}