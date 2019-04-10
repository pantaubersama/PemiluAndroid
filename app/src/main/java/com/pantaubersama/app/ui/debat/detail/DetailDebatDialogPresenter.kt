package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.OpiniumServiceInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.user.Profile
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

/**
 * @author edityomurti on 06/03/2019 18:19
 */

class DetailDebatDialogPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor,
    private val opiniumServiceInteractor: OpiniumServiceInteractor
) : BasePresenter<DetailDebatDialogView>() {
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
}