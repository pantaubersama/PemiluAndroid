package com.pantaubersama.app.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

/**
 * Created by ali on 19/12/17.
 */
@Module
class SharedPreferenceModule(val context: Context){
    @Provides
    fun provideSharedPreference(): SharedPreferences = context.getSharedPreferences("pantau_bersama", Context.MODE_PRIVATE)
}