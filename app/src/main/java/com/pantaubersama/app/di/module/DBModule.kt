package com.pantaubersama.app.di.module

import android.content.Context
import com.pantaubersama.app.data.db.AppDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {
    @Provides
    @Singleton
    fun provideDb(context: Context): AppDB {
        return AppDB.getInstance(context)
    }
}