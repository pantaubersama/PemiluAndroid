package com.pantaubersama.app.di.module

import android.content.Context
import com.pantaubersama.app.data.local.cache.DataCache
import dagger.Module
import dagger.Provides

/**
 * Created by ali on 19/12/17.
 */
@Module
class SharedPreferenceModule(val context: Context) {

    @Provides
    fun provideDataCache(): DataCache {
        return DataCache.getInstance(context)
    }
}