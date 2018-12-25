package com.pantaubersama.app.base

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDexApplication
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
    var appComponent: AppComponent? = null
    var activityComponent: ActivityComponent? = null
    var serviceComponent: ServiceComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberUtil())
        }
    }

    fun createActivityComponent(activity: Activity?): ActivityComponent? {
        activityComponent = appComponent?.withActivityComponent(
                ActivityModule(activity!!),
                ApiModule(),
                ConnectionModule(this),
                SharedPreferenceModule(this),
                RxSchedulersModule())
        return activityComponent
    }

    fun releaseActivityComponent() {
        activityComponent = null
    }

    fun createServiceComponent(context: Context?): ServiceComponent? {
        serviceComponent = appComponent?.withServiceComponent(
                ServiceModule(context),
                ApiModule(),
                ConnectionModule(this),
                SharedPreferenceModule(this),
                RxSchedulersModule())
        return serviceComponent
    }

    fun releaseServiceComponent() {
        serviceComponent = null
    }
}