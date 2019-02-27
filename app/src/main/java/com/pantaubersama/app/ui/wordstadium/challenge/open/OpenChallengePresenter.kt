package com.pantaubersama.app.ui.wordstadium.challenge.open

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import javax.inject.Inject

class OpenChallengePresenter @Inject constructor(
        private val profileInteractor: ProfileInteractor,
        private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<OpenChallengeView>() {

    fun getUserProfile() {
        view?.showProfile(profileInteractor.getProfile())
    }

    fun getConvertLink(url: String) {
        view?.showLoading()
        disposables.add(wordStadiumInteractor.getConvertLink(url)
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe(
                        {
                            view?.onSuccessConvertLink(it)
                        },
                        {
                            view?.showError(it)
                        }
                )
        )
    }
}