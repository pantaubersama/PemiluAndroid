package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.*
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.State
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class MengujiPresenter @Inject constructor(
    private val bannerInfoInteractor: BannerInfoInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<MengujiView>() {

    private val isPublik: Boolean
        get() = view?.isPublik ?: true

    fun getBanner() {
        view?.showLoading()
        disposables += bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_DEBAT)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showBanner(it)
            }, {
                view?.showError(it)
                view?.hideBanner()
            })
    }

    fun getDebatLive() {
        val debatList = (0..3).map {
            DebatItem.LiveNow(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", "dummy"))
        }
        view?.showDebatLive(debatList)
    }

    fun getDebatComingSoon() {
        val debatList = (0..2).map {
            DebatItem.ComingSoon(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", "dummy"),
                "24 Maret 2019", "16:00 - 17:00")
        }
        view?.showDebatComingSoon(debatList)
    }

    fun getDebatDone() {
        val debatList = (0..2).map {
            DebatItem.Done(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", "dummy"),
                70, 70, 50)
        }
        view?.showDebatDone(debatList)
    }

    fun getDebatOpen() {
        view?.showDebatOpen(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(ChallengeConstants.PROGRESS_ON_GOING)
        else
            wordStadiumInteractor.getPersonalChallenge(ChallengeConstants.PROGRESS_ON_GOING)

        disposables += request
            .subscribe({ list ->
                val debatList = list.take(3)
                    .map { it.toDebatItemChallenge() }
                view?.showDebatOpen(State.Success, debatList, list.size > 3)
            }, {
                view?.showDebatOpen(State.Error(it.message), emptyList(), false)
            })
    }
}
