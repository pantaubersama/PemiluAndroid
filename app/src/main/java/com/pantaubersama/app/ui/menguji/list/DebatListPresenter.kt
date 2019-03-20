package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.data.model.debat.ChallengeData
import com.pantaubersama.app.utils.PantauConstants.Debat.Title
import io.reactivex.Single
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class DebatListPresenter @Inject constructor(
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<DebatListView>() {

    fun getChallenges(title: String, page: Int) {
        if (page == 1) view?.showLoading()

        disposables += getRequest(title, page)
            .doOnEvent { _, _ ->
                if (page == 1) view?.dismissLoading()
            }
            .subscribe({
                if (page == 1) {
                    view?.showChallenges(it.challenges)
                } else {
                    view?.showMoreChallenges(it.challenges)
                }
                if ((it.meta.pages?.total ?: 1) <= page) {
                    view?.setNoMoreItems()
                }
            }, {
                // TODO: handle error "expected :page in 1..1; got 2" from backend instead
                if (it.message?.contains("page") == false) {
                    view?.showError(it)
                }
            })
    }

    private fun getRequest(title: String, page: Int): Single<ChallengeData> = with(wordStadiumInteractor) {
        when (title) {
            Title.PUBLIK_LIVE_NOW -> getPublicChallenge(Progress.LIVE_NOW, page)
            Title.PUBLIK_COMING_SOON -> getPublicChallenge(Progress.COMING_SOON, page)
            Title.PUBLIK_DONE -> getPublicChallenge(Progress.DONE, page)
            Title.PUBLIK_CHALLENGE -> getPublicChallenge(Progress.CHALLENGE, page)
            Title.PERSONAL_CHALLENGE_IN_PROGRESS -> getPersonalChallenge(Progress.CHALLENGE, page)
            Title.PERSONAL_COMING_SOON -> getPersonalChallenge(Progress.COMING_SOON, page)
            Title.PERSONAL_DONE -> getPersonalChallenge(Progress.DONE, page)
            Title.PERSONAL_CHALLENGE -> getPersonalChallenge(Progress.CHALLENGE, page)
            else -> throw IllegalArgumentException("Unknown title")
        }
    }
}
