package com.pantaubersama.app.base

import android.app.Activity
import android.app.Service
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.di.component.AppComponent
import com.pantaubersama.app.di.component.DaggerAppComponent
import com.pantaubersama.app.di.component.ServiceComponent
import com.pantaubersama.app.di.module.* // ktlint-disable
import com.pantaubersama.app.utils.TimberUtil
import timber.log.Timber

/**
 * @author edityomurti on 14/12/2018 14:55
 */
class BaseApp : MultiDexApplication() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        } else {
            Timber.plant(TimberUtil())
        }
    }

    fun createActivityComponent(activity: Activity?): ActivityComponent? {
        return appComponent.withActivityComponent(
                ActivityModule(activity!!))
    }

    fun releaseActivityComponent() {

    }

    fun createServiceComponent(service: Service): ServiceComponent? {
        return appComponent.withServiceComponent(
                ServiceModule(service))
    }
}