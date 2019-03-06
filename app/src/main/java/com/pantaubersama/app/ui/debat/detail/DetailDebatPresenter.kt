package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BasePresenter
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
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<DetailDebatView>() {
    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getStatementSourcePreview(url: String) {
        view?.showLoadingStatementSource()
        disposables += wordStadiumInteractor.getConvertLink(url)
            .doOnEvent { _, _ -> view?.dismissLoadingStatementSource() }
            .subscribe(
                {
                    it?.html?.let { html -> view?.showStatementSource(html) }
                },
                {
                    view?.onErrorStatementSource(it)
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
}