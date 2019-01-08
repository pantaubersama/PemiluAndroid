package com.pantaubersama.app.di.module

import android.content.Context
import com.pantaubersama.app.utils.ConnectionState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ali on 22/11/17.
 */
@Module
class ConnectionModule {
    @Provides
    @Singleton
    fun provideConnection(context: Context): ConnectionState {
        return ConnectionState(context)
    }
}