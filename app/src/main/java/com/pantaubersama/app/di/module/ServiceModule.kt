package com.pantaubersama.app.di.module

import android.app.Service
import com.pantaubersama.app.di.scope.ServiceScope
import dagger.Module
import dagger.Provides

/**
 * Created by ali on 19/10/17.
 */
@Module
class ServiceModule(private val service: Service) {
    @Provides
    @ServiceScope
    fun provideService(): Service = service
}