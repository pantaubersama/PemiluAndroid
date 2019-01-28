package com.pantaubersama.app.background.firebase

import com.pantaubersama.app.data.interactors.LoginInteractor
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PantauFirebaseMessagingPresenter @Inject constructor(private val loginInteractor: LoginInteractor) {
    var disposables: CompositeDisposable = CompositeDisposable()

    fun saveFirebaseNewToken(firebaseToken: String) {
        disposables.add(
            loginInteractor.exchangeToken(firebaseToken)
                .subscribe(
                    {
                        disposables.clear()
                    },
                    {
                        disposables.clear()
                    }
                )
        )
    }
}