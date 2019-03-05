package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.utils.PantauConstants.Debat.Title
import io.reactivex.Single
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class DebatListPresenter @Inject constructor(
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<DebatListView>() {

    fun getChallenges(title: String) {
        view?.showLoading()

        disposables += getRequest(title)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showChallenge(it)
            }, {
                view?.showError(it)
            })
    }

    private fun getRequest(title: String): Single<List<Challenge>> = with(wordStadiumInteractor) {
        when (title) {
            Title.PUBLIK_LIVE_NOW -> getPublicChallenge(Progress.LIVE_NOW)
            Title.PUBLIK_COMING_SOON -> getPublicChallenge(Progress.COMING_SOON)
            Title.PUBLIK_DONE -> getPublicChallenge(Progress.DONE)
            Title.PUBLIK_CHALLENGE -> getPublicChallenge(Progress.CHALLENGE)
            Title.PERSONAL_CHALLENGE_IN_PROGRESS -> getPersonalChallenge(Progress.CHALLENGE)
            Title.PERSONAL_COMING_SOON -> getPersonalChallenge(Progress.COMING_SOON)
            Title.PERSONAL_DONE -> getPersonalChallenge(Progress.DONE)
            Title.PERSONAL_CHALLENGE -> getPersonalChallenge(Progress.CHALLENGE)
            else -> Single.just(emptyList())
        }
    }
}
