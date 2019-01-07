package com.pantaubersama.app.base

import io.reactivex.disposables.CompositeDisposable

/**
 * @author edityomurti on 14/12/2018 17:35
 */
abstract class BasePresenter<V : BaseView> {
    var view: V? = null
    var disposables: CompositeDisposable? = null

    @Suppress("UNCHECKED_CAST")
    fun attach(view: BaseView) {
        this.view = view as V
        disposables = CompositeDisposable()
    }

    fun detach() {
        disposables?.run { if (!isDisposed) dispose() }
    }
}