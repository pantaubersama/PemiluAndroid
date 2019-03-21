package com.pantaubersama.app.ui.wordstadium.challenge.open

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.OpiniumServiceInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class OpenChallengePresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor,
    private val opiniumServiceInteractor: OpiniumServiceInteractor
) : BasePresenter<OpenChallengeView>() {

    fun getUserProfile() {
        view?.showProfile(profileInteractor.getProfile())
    }

    fun getTweetPreview(url: String) {
        view?.showLoadingUrlPreview()
        if (url.startsWith("https://twitter.com/", true) || url.startsWith("twitter.com/", true)) {
            disposables += wordStadiumInteractor.getConvertLink(url)
                .subscribe(
                    {
                        view?.onSuccessTweetPreview(it)
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
}