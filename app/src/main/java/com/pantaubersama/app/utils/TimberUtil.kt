package com.pantaubersama.app.utils

import timber.log.Timber

class TimberUtil : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // send log through crash library
    }
}