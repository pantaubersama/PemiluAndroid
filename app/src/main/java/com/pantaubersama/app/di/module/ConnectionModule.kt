package com.pantaubersama.app.di.module

import android.content.Context
import com.pantaubersama.app.utils.ConnectionState
import dagger.Module
import dagger.Provides

/**
 * Created by ali on 22/11/17.
 */
@Module
class ConnectionModule(val context: Context){
    @Provides
    fun provideConnection(): ConnectionState {
        return ConnectionState(context)
    }
}