package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.OpiniumServiceInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.user.Profile
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

/**
 * @author edityomurti on 01/03/2019 18:05
 */

class DetailDebatPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor,
    private val opiniumServiceInteractor: OpiniumServiceInteractor
) : BasePresenter<DetailDebatView>() {
    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getTweetPreview(url: String) {
        view?.showLoadingUrlPreview()
        if (url.startsWith("https://twitter.com/", true) || url.startsWith("twitter.com/", true)) {
            disposables += wordStadiumInteractor.getConvertLink(url)
                .subscribe(
                    {
                        view?.showTweetPreview(it)
                        view?.dismissLoadingUrlPreview()
                    },
                    {
                        getUrlPreview(url)
                    }
                )
        } else {
            getUrlPreview(url)
        }
    }

    private fun getUrlPreview(url: String) {
        view?.showLoadingUrlPreview()
        disposables += opiniumServiceInteractor.getUrlMeta(url)
            .doOnEvent { _, _ -> view?.dismissLoadingUrlPreview() }
            .subscribe(
                {
                    view?.showUrlPreview(it)
                },
                {
                    view?.onErrorUrlPreview(it)
                }
            )
    }

    fun confirmOpponentCandidate(challengeId: String, audienceId: String) {
        view?.showLoadingConfirmOpponentCandidate()
        disposables += wordStadiumInteractor.confirmOpponentCandidate(challengeId, audienceId)
            .doOnEvent { view?.dismissLoadingConfirmOpponentCandidate() }
            .subscribe(
                {
                    view?.onSuccessConfirmOpponentCandidate(audienceId)
                },
                {
                    view?.showError(it)
                    view?.onErrorConfirmOpponentcandidate(it)
                }
            )
    }

    fun askAsOpponent(challengeId: String) {
        view?.showLoadingAskAsOpponent()
        disposables += wordStadiumInteractor.askAsOpponent(challengeId)
            .subscribe(
                {
//                    Handler().postDelayed({  /* @edityo 5/3/19  delayed bcs response getItemChallenge not changed immediately */
                        view?.dismissLoadingAskAsOpponent()
                        view?.onSuccessAskAsOpponent()
//                    }, 1000)
                },
                {
                    view?.dismissLoadingAskAsOpponent()
                    view?.showError(it)
                    view?.onErrorAskAsOpponent(it)
                }
            )
    }

    fun getChallengeItem(id: String) {
        view?.showLoading()
        disposables += wordStadiumInteractor.getChallengeItem(id)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe(
                {
                    view?.showChallenge(it)
                },
                {
                    view?.showError(it)
                    view?.onErrorGetChallenge(it)
                }
            )
    }

    fun approveDirect(challengeId: String) {
        view?.showLoadingApproveDirect()
        disposables += wordStadiumInteractor.approveDirect(challengeId)
            .doOnEvent { view?.dismissLoadingApproveDirect() }
            .subscribe(
                {
                    view?.onSuccessApproveDirect()
                },
                {
                    view?.showError(it)
                    view?.onErrorApproveDirect(it)
                }
            )
    }

    fun rejectDirect(challengeId: String, reason: String) {
        view?.showLoadingRejectDirect()
        disposables += wordStadiumInteractor.rejectDirect(challengeId, reason)
            .doOnEvent { view?.dismissLoadingRejectDirect() }
            .subscribe(
                {
                    view?.onSuccessRejectDirect()
                },
                {
                    view?.showError(it)
                    view?.onErrorRejectDirect(it)
                }
            )
    }

    fun putLikeChallenge(challengeId: String) {
        disposables += wordStadiumInteractor.putLikeChallenge(challengeId)
            .subscribe(
                {
                    view?.onSuccessLikeChallenge()
                },
                {
                    view?.onErrorLikeChallenge(it)
                }
            )
    }

    fun unlikeChallenge(challengeId: String) {
        disposables += wordStadiumInteractor.unlikeChallenge(challengeId)
            .subscribe(
                {
                    view?.onSuccessUnikeChallenge()
                },
                {
                    view?.onErrorUnlikeChallenge(it)
                }
            )
    }

    fun promoteChallenge(challengeId: String) {
        view?.showLoadingPromoteChallenge()
        disposables += wordStadiumInteractor.promoteOpenChallenge(challengeId)
            .doOnEvent { view?.dismissLoadingPromoteChallenge() }
            .subscribe(
                {
                    view?.onSuccessPromoteChallenge()
                },
                {
                    view?.onErrorPromoteChallenge(it)
                }
            )
    }
}