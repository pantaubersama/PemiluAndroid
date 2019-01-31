package com.pantaubersama.app.base

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.di.component.AppComponent
import com.pantaubersama.app.di.component.DaggerAppComponent
import com.pantaubersama.app.di.module.* // ktlint-disable
import com.pantaubersama.app.utils.TimberUtil
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import com.orhanobut.logger.DiskLogAdapter



/**
 * @author edityomurti on 14/12/2018 14:55
 */
class BaseApp : MultiDexApplication() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Twitter.initialize(
            TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(getString(R.string.twitter_api_key), getString(R.string.twitter_secret_key)))
                .debug(true)
                .build()
        )
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(AndroidLogAdapter())
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    Logger.log(priority, tag, message, t)
                }
            })
            Stetho.initializeWithDefaults(this)
        } else {
            Timber.plant(TimberUtil())
        }
    }
}