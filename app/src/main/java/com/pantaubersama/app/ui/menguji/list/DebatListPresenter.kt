package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.ChallengeConstants
import com.pantaubersama.app.utils.PantauConstants.Debat.Title
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class DebatListPresenter @Inject constructor(
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<DebatListView>() {

    fun getDebatItems(title: String) {
        when (title) {
            Title.PUBLIK_LIVE_NOW -> getDebatLive()
            Title.PUBLIK_COMING_SOON, Title.PERSONAL_COMING_SOON -> getDebatComingSoon()
            Title.PUBLIK_DONE, Title.PERSONAL_DONE -> getDebatDone()
            Title.PUBLIK_CHALLENGE, Title.PERSONAL_CHALLENGE_IN_PROGRESS, Title.PERSONAL_CHALLENGE ->
                getDebatOpen(title)
        }
    }

    fun getDebatLive() {
        view?.showChallenge(emptyList())
    }

    fun getDebatComingSoon() {
        view?.showChallenge(emptyList())
    }

    fun getDebatDone() {
        view?.showChallenge(emptyList())
    }

    fun getDebatOpen(title: String) {
        view?.showLoading()

        val request = if (title == Title.PUBLIK_CHALLENGE)
            wordStadiumInteractor.getPublicChallenge(ChallengeConstants.Progress.ON_GOING)
        else
            wordStadiumInteractor.getPersonalChallenge(ChallengeConstants.Progress.ON_GOING)

        disposables += request
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showChallenge(it)
            }, {
                view?.showError(it)
            })
    }
}
