package com.pantaubersama.app.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by alimustofa on 29/01/18.
 */
class RxSchedulers {
    fun io(): Scheduler {
        return Schedulers.io()
    }

    fun newThread(): Scheduler{
        return Schedulers.newThread()
    }

    fun computation(): Scheduler{
        return Schedulers.computation()
    }

    //foreground
    fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}