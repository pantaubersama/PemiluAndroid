package com.pantaubersama.app.di.component

import com.pantaubersama.app.di.module.* // ktlint-disable
import dagger.Component
import javax.inject.Singleton

/**
 * Created by ali on 02/10/17.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun withActivityComponent(
        activityModule: ActivityModule,
        apiModule: ApiModule,
        connectionModule: ConnectionModule,
        sharedPreferenceModule: SharedPreferenceModule,
        rxSchedulersModule: RxSchedulersModule
    ): ActivityComponent

    fun withServiceComponent(
        serviceModule: ServiceModule,
        apiModule: ApiModule,
        connectionModule: ConnectionModule,
        sharedPreferenceModule: SharedPreferenceModule,
        rxSchedulersModule: RxSchedulersModule
    ): ServiceComponent
}