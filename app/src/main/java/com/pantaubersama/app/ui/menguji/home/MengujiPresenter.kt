package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.ChallengeConstants
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.State
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MengujiPresenter @Inject constructor(
    private val bannerInfoInteractor: BannerInfoInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<MengujiView>() {

    private val isPublik: Boolean
        get() = view?.isPublik ?: true

    private val emptyChallenges = BehaviorSubject.create<Boolean>()

    fun getBanner() {
        view?.showLoading()
        disposables += bannerInfoInteractor.getBannerInfo(
            if (isPublik) PantauConstants.BANNER_DEBAT_PUBLIC else PantauConstants.BANNER_DEBAT_PERSONAL
        )
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showBanner(it)
            }, {
                view?.showError(it)
                view?.hideBanner()
            })
    }

    fun getChallengeLive() {
        view?.showChallengeLive(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(ChallengeConstants.Progress.LIVE_NOW)
        else
            wordStadiumInteractor.getPersonalChallenge(ChallengeConstants.Progress.LIVE_NOW)

        disposables += request
            .subscribe({
                view?.showChallengeLive(State.Success, it.take(3), it.size > 3)
                emptyChallenges.onNext(it.isEmpty())
            }, {
                view?.showChallengeLive(State.Error(it.message), emptyList(), false)
            })
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
                emptyChallenges.onNext(it.isEmpty())
            }, {
                view?.showChallengeComingSoon(State.Error(it.message), emptyList(), false)
            })
    }

    fun getChallengeDone() {
        view?.showChallengeDone(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(ChallengeConstants.Progress.DONE)
        else
            wordStadiumInteractor.getPersonalChallenge(ChallengeConstants.Progress.DONE)

        disposables += request
            .subscribe({
                view?.showChallengeDone(State.Success, it.take(3), it.size > 3)
                emptyChallenges.onNext(it.isEmpty())
            }, {
                view?.showChallengeDone(State.Error(it.message), emptyList(), false)
            })
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
                emptyChallenges.onNext(it.isEmpty())
            }, {
                view?.showChallengeOngoing(State.Error(it.message), emptyList(), false)
            })
    }

    fun observeEmptyChallenges() {
        disposables += emptyChallenges.buffer(4)
            .subscribe { emptyResults ->
                view?.showAllChallengeEmpty(emptyResults.all { it })
            }
    }
}
