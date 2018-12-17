package com.pantaubersama.app.base

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.di.component.AppComponent
import com.pantaubersama.app.di.component.DaggerAppComponent
import com.pantaubersama.app.di.component.ServiceComponent
import com.pantaubersama.app.di.module.*

/**
 * @author edityomurti on 14/12/2018 14:55
 */
class BaseApp : MultiDexApplication() {
    var appComponent: AppComponent? = null
    var activityComponent: ActivityComponent? = null
    var serviceComponent: ServiceComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    fun createActivityComponent(activity: Activity?): ActivityComponent?{
        activityComponent = appComponent?.withActivityComponent(
                ActivityModule(activity!!),
                NetworkModule(),
                ApiModule(),
                ConnectionModule(this),
                SharedPreferenceModule(this),
                RxSchedulersModule())
        return activityComponent
    }

    fun releaseActivityComponent(){
        activityComponent = null
    }

    fun createServiceComponent(context: Context?): ServiceComponent?{
        serviceComponent = appComponent?.withServiceComponent(
                ServiceModule(context),
                NetworkModule(),
                ApiModule(),
                ConnectionModule(this),
                SharedPreferenceModule(this),
                RxSchedulersModule())
        return serviceComponent
    }

    fun releaseServiceComponent(){
        serviceComponent = null
    }
}