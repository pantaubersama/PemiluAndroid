package com.pantaubersama.app.ui.debat

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.WordsPNHandler
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

/**
 * @author edityomurti on 15/02/2019 12:31
 */
class DebatPresenter @Inject constructor(
    private val wordStadiumInteractor: WordStadiumInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<DebatView>() {
    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getWordsFighter(challengeId: String) {
        view?.showLoadingWordsFighter()
        disposables += wordStadiumInteractor.getWordsFighter(challengeId)
            .doOnEvent { _, _ -> view?.dismissLoadingWordsFighter() }
            .subscribe(
                {
                    if (!it.isEmpty()) {
                        view?.showWordsFighter(it.asReversed())
                    } else {
                        view?.onEmptyWordsFighter()
                    }
                    view?.updateMyTimeLeft()
                },
                {
                    view?.showError(it)
                    view?.onErrorGetWordsFighter(it)
                }
            )
    }

    fun postWordsFighter(challengeId: String, words: String) {
        disposables += wordStadiumInteractor.postWordsFighter(challengeId, words)
            .subscribe(
                {
                    view?.onSuccessPostWordsFighter(it)
                    view?.updateMyTimeLeft()
                },
                {
                    view?.showError(it)
                    view?.onFailedPostWordsFighter(it)
                }
            )
    }

    fun getWordsAudience(challengeId: String) {
        view?.showLoadingKomentar()
        disposables += wordStadiumInteractor.getWordsAudience(challengeId)
            .doOnEvent { _, _ -> view?.dismissLoadingKomentar() }
            .subscribe(
                {
                    if (!it.isEmpty()) {
                        view?.showKomentar(it)
                    } else {
                        view?.onEmptyWordsAudience()
                    }
                },
                {
                    view?.showError(it)
                    view?.onErrorGetWordsAudience(it)
                }
            )
    }

    fun postWordsAudience(challengeId: String, words: String) {
        disposables += wordStadiumInteractor.postWordsAudience(challengeId, words)
            .subscribe(
                {
                    view?.onSuccessPostWordsAudience(it)
                },
                {
                    view?.showError(it)
                    view?.onFailedPostWordsAudience(it)
                }
            )
    }

    fun setOnGotNewWordsListener(onGotNewWordsListener: WordsPNHandler.OnGotNewWordsListener?) {
        wordStadiumInteractor.setOnGotNewWordsListener(onGotNewWordsListener)
    }

    fun putWordsClap(wordId: String) {
        disposables += wordStadiumInteractor.putWordsClap(wordId)
            .subscribe(
                {
                    view?.onSuccessWordsClap()
                },
                {
//                    view?.showError(it)
                    view?.onErrorWordsClap(it)
                }
            )
    }
}