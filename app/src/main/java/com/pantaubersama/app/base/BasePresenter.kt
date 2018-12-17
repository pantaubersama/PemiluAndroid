package com.pantaubersama.app.base

import io.reactivex.disposables.CompositeDisposable

/**
 * @author edityomurti on 14/12/2018 17:35
 */
abstract class BasePresenter<V : BaseView> {
    var view: V? = null
    val disposable: CompositeDisposable? = null

    fun attach(view: BaseView) {
        this.view = view as V
    }

    fun detach() {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }
}