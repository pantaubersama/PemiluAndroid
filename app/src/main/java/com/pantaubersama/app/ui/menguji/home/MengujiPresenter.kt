package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
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

    fun getChallengeCarousel() {
        view?.showChallengeLive(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(Progress.LIVE_NOW, 1, MAX_CAROUSEL_ITEM)
        else
            wordStadiumInteractor.getPersonalChallenge(Progress.IN_PROGRESS, 1, MAX_CAROUSEL_ITEM)

        disposables += request
            .subscribe({
                val hasMore = (it.meta.pages?.total ?: 1) > 1
                view?.showChallengeLive(State.Success, it.challenges, hasMore)
                emptyChallenges.onNext(it.challenges.isEmpty())
            }, {
                view?.showChallengeLive(State.Error(it.message), emptyList(), false)
            })
    }

    fun getChallengeComingSoon() {
        view?.showChallengeComingSoon(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(Progress.COMING_SOON, 1, MAX_SECTION_ITEM)
        else
            wordStadiumInteractor.getPersonalChallenge(Progress.COMING_SOON, 1, MAX_SECTION_ITEM)

        disposables += request
            .subscribe({
                val hasMore = (it.meta.pages?.total ?: 1) > 1
                view?.showChallengeComingSoon(State.Success, it.challenges, hasMore)
                emptyChallenges.onNext(it.challenges.isEmpty())
            }, {
                view?.showChallengeComingSoon(State.Error(it.message), emptyList(), false)
            })
    }

    fun getChallengeDone() {
        view?.showChallengeDone(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(Progress.DONE, 1, MAX_SECTION_ITEM)
        else
            wordStadiumInteractor.getPersonalChallenge(Progress.DONE, 1, MAX_SECTION_ITEM)

        disposables += request
            .subscribe({
                val hasMore = (it.meta.pages?.total ?: 1) > 1
                view?.showChallengeDone(State.Success, it.challenges, hasMore)
                emptyChallenges.onNext(it.challenges.isEmpty())
            }, {
                view?.showChallengeDone(State.Error(it.message), emptyList(), false)
            })
    }

    fun getChallengeOngoing() {
        view?.showChallengeOngoing(State.Loading, emptyList(), false)

        val request = if (isPublik)
            wordStadiumInteractor.getPublicChallenge(Progress.CHALLENGE, 1, MAX_SECTION_ITEM)
        else
            wordStadiumInteractor.getPersonalChallenge(Progress.CHALLENGE, 1, MAX_SECTION_ITEM)

        disposables += request
            .subscribe({
                val hasMore = (it.meta.pages?.total ?: 1) > 1
                view?.showChallengeOngoing(State.Success, it.challenges, hasMore)
                emptyChallenges.onNext(it.challenges.isEmpty())
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

    companion object {
        private const val MAX_CAROUSEL_ITEM = 5
        private const val MAX_SECTION_ITEM = 3
    }
}
