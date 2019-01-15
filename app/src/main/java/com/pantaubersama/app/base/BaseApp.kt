package com.pantaubersama.app.base

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
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
import timber.log.Timber

/**
 * @author edityomurti on 14/12/2018 14:55
 */
class BaseApp : MultiDexApplication() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
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
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        } else {
            Timber.plant(TimberUtil())
        }
    }
}