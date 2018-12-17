package com.pantaubersama.app.di.component

import com.pantaubersama.app.di.module.*
import dagger.Component
import javax.inject.Singleton

/**
 * Created by ali on 02/10/17.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent{
    fun withActivityComponent(activityModule: ActivityModule,
                              networkModule: NetworkModule,
                              apiModule: ApiModule,
                              connectionModule: ConnectionModule,
                              sharedPreferenceModule: SharedPreferenceModule,
                              rxSchedulersModule: RxSchedulersModule): ActivityComponent

    fun withServiceComponent(serviceModule: ServiceModule,
                             networkModule: NetworkModule,
                             apiModule: ApiModule,
                             connectionModule: ConnectionModule,
                             sharedPreferenceModule: SharedPreferenceModule,
                             rxSchedulersModule: RxSchedulersModule): ServiceComponent
}