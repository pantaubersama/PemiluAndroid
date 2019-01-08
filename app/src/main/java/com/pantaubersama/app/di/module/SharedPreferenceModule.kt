package com.pantaubersama.app.di.module

import android.content.Context
import com.pantaubersama.app.data.local.cache.DataCache
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ali on 19/12/17.
 */
@Module
class SharedPreferenceModule {

    @Provides
    @Singleton
    fun provideDataCache(context: Context): DataCache {
        return DataCache.getInstance(context)
    }
}