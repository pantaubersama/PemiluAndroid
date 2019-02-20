package com.pantaubersama.app.ui.wordstadium.challenge.open

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import javax.inject.Inject

class OpenChallengePresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor
) :
        BasePresenter<OpenChallengeView>() {

    fun getUserProfile() {
        view?.showProfile(profileInteractor.getProfile())
    }

    fun openChallenge(
        token: String?,
        topicList: String?,
        statement: String?,
        statementSource: String?,
        showTimeAt: String?,
        timeLimit: Int
    ) {
        view?.showLoading()
        disposables.add(
                wordStadiumInteractor.openChallenge(
                        token,
                        topicList,
                        statement,
                        statementSource,
                        showTimeAt,
                        timeLimit
                )
                        .subscribe(
                                {
                                    view?.dismissLoading()
                                    view?.onSuccessOpenChallenge()
                                },
                                {
                                    view?.dismissLoading()
                                    view?.showError(it)
                                }
                        )
        )
    }
}