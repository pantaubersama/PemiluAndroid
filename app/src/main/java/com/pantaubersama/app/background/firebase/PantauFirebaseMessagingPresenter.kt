package com.pantaubersama.app.background.firebase

import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.WordItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class PantauFirebaseMessagingPresenter @Inject constructor(
    private val loginInteractor: LoginInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor
) {
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

    fun handleWords(wordItem: WordItem) {
        disposables += wordStadiumInteractor.handleWords(wordItem)
            .subscribe(
                {
                    disposables.clear()
                },
                {
                    disposables.clear()
                }
            )
    }
}