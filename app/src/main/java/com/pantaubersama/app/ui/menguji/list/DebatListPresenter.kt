package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.*
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
        val debatList = (0..9).map {
            DebatItem.LiveNow(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", ""))
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatComingSoon() {
        val debatList = (0..9).map {
            DebatItem.ComingSoon(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", ""),
                "24 Maret 2019", "16:00 - 17:00")
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatDone() {
        val debatList = (0..9).map {
            DebatItem.Done(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", ""),
                70, 70, 50)
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatOpen(title: String) {
        view?.showLoading()

        val request = if (title == Title.PUBLIK_CHALLENGE)
            wordStadiumInteractor.getPublicChallenge(ChallengeConstants.PROGRESS_ON_GOING)
        else
            wordStadiumInteractor.getPersonalChallenge(ChallengeConstants.PROGRESS_ON_GOING)

        disposables += request
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({ list ->
                val debatList = list.map { it.toDebatItemChallenge() }
                view?.showDebatItems(debatList)
            }, {
                view?.showError(it)
            })
    }
}
