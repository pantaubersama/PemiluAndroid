package com.pantaubersama.app.utils.extensions

import com.pantaubersama.app.utils.Optional
import io.reactivex.Single

fun <T : Any> Single<T>.optional(): Single<Optional<T>> {
    return this.map { Optional.Some(it) as Optional<T> }
        .onErrorReturn { Optional.None(it) }
}