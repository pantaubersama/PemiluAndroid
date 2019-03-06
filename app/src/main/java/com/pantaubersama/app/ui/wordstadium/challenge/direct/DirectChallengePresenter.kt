package com.pantaubersama.app.ui.wordstadium.challenge.direct

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.UserInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import javax.inject.Inject

class DirectChallengePresenter @Inject constructor(
        private val profileInteractor: ProfileInteractor,
        private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<DirectChallengeView>() {

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
        view?.showLoading()
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
}