package com.pantaubersama.app.ui.debat

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.user.Profile
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
                        view?.showWordsFighter(it)
                    } else {
                        view?.onEmptyWordsFighter()
                    }
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
                },
                {
                    view?.showError(it)
                    view?.onErrorGetWordsFighter(it)
                }
            )
    }

//    fun getKomentar() {
//        view?.showLoadingKomentar()
//        disposables.add(debatInteractor.getKomentar()
//            .subscribe(
//                {
//                    view?.dismissLoadingKomentar()
//                    view?.showKomentar(it)
//                },
//                {
//                }
//            )
//        )
//    }
}