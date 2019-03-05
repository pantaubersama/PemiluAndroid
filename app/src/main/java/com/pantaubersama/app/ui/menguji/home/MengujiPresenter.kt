package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.ChallengeConstants
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

    fun getChallengeLive() {
        view?.showChallengeLive(emptyList())
    }

    fun getChallengeComingSoon() {
        view?.showChallengeComingSoon(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(ChallengeConstants.Progress.COMING_SOON)
        else
            wordStadiumInteractor.getPersonalChallenge(ChallengeConstants.Progress.COMING_SOON)

        disposables += request
            .subscribe({
                view?.showChallengeComingSoon(State.Success, it.take(3), it.size > 3)
            }, {
                view?.showChallengeComingSoon(State.Error(it.message), emptyList(), false)
            })
    }

    fun getChallengeDone() {
        view?.showChallengeDone(emptyList())
    }

    fun getChallengeOngoing() {
        view?.showChallengeOngoing(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(ChallengeConstants.Progress.CHALLENGE)
        else
            wordStadiumInteractor.getPersonalChallenge(ChallengeConstants.Progress.CHALLENGE)

        disposables += request
            .subscribe({
                view?.showChallengeOngoing(State.Success, it.take(3), it.size > 3)
            }, {
                view?.showChallengeOngoing(State.Error(it.message), emptyList(), false)
            })
    }
}
