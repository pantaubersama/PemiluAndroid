package com.pantaubersama.app.ui.wordstadium.challenge.direct

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.OpiniumServiceInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class DirectChallengePresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor,
    private val opiniumServiceInteractor: OpiniumServiceInteractor
) : BasePresenter<DirectChallengeView>() {

    fun getUserProfile() {
        view?.showProfile(profileInteractor.getProfile())
    }

    fun getTweetPreview(url: String) {
        view?.showLoading()
        if (url.startsWith("https://twitter.com/", true) || url.startsWith("twitter.com/", true)) {
            disposables += wordStadiumInteractor.getConvertLink(url)
                .subscribe(
                    {
                        view?.onSuccessTweetPreview(it)
                        view?.dismissLoading()
                    },
                    {
                        getUrlPreview(url)
                    }
                )
        } else {
            getUrlPreview(url)
        }
    }

    fun searchLawanDebat(keyword: String, page: Int, perPage: Int) {
        view?.showLoading()
        disposables.add(
                wordStadiumInteractor.searchLawanDebat(keyword, page, perPage)
                        .subscribe(
                                {
                                    if (page == 1) {
                                        view?.dismissLoading()
                                        if (it.size != 0) {
                                            view?.bindData(it)
                                        } else {
                                            view?.showEmptyData()
                                        }
                                    }
                                },
                                {
                                    if (page == 1) {
                                        view?.dismissLoading()
                                        view?.showFailedGetDataAlert()
                                    }
                                    it.printStackTrace()
                                    view?.showError(it)
                                }
                        )
        )
    }

    fun searchPerson(keyword: String, page: Int, perPage: Int) {
        view?.showLoadingUrlPreview()
        disposables.add(
                wordStadiumInteractor.searchPerson(keyword, page, perPage)
                        .subscribe(
                                {
                                    if (page == 1) {
                                        view?.dismissLoading()
                                        if (it.size != 0) {
                                            view?.bindData(it)
                                        } else {
                                            view?.showEmptyData()
                                        }
                                    }
                                },
                                {
                                    if (page == 1) {
                                        view?.dismissLoading()
                                        view?.showFailedGetDataAlert()
                                    }
                                    it.printStackTrace()
                                    view?.showError(it)
                                }
                        )
        )
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