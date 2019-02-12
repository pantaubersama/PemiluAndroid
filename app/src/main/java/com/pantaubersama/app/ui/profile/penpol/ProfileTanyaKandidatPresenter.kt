package com.pantaubersama.app.ui.profile.penpol

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import javax.inject.Inject

class ProfileTanyaKandidatPresenter @Inject constructor(
    private val tanyaKandidatInteractor: TanyaKandidatInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<ProfileTanyaKandidatView>() {

    fun getTanyaKandidatList(page: Int, perPage: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(
            tanyaKandidatInteractor.getMyTanyaKandidatList(page, perPage)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it?.questions != null) {
                                if (it.questions.size != 0) {
                                    view?.bindDataTanyaKandidat(it.questions)
                                } else {
                                    view?.showEmptyDataAlert()
                                }
                            } else {
                                view?.showFailedGetDataAlert()
                            }
                        } else {
                            if (it?.questions?.size != 0) {
                                it?.questions?.let { it1 -> view?.bindNextDataTanyaKandidat(it1) }
                            } else {
                                view?.showEmptyNextDataAlert()
                            }
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showFailedGetDataAlert()
                        view?.showError(it)
                    }
                )
        )
    }

    fun upVoteQuestion(id: String, questionCalass: String, isLiked: Boolean, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .upVoteQuestion(
                    id,
                    questionCalass
                )
                .subscribe(
                    {
                        view?.onItemUpVoted()
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(isLiked, position)
                    }
                )
        )
    }

    fun deleteItem(id: String, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .deleteQuestions(id)
                .subscribe(
                    {
                        view?.onItemDeleted(position)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedDeleteItemAlert()
                    }
                )
        )
    }

    fun unVoteQuestion(id: String, className: String, liked: Boolean, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .unVoteQuestion(id, className)
                .subscribe(
                    {
                        view?.onItemUpVoted()
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(liked, position)
                    }
                )
        )
    }

    fun getProfile() {
        view?.bindProfile(profileInteractor.getProfile())
    }

    fun getUserTanyaKandidat(page: Int, userId: String, perPage: Int) {
        if (page == 1) {
            view?.showLoading()
        }
        disposables.add(
            tanyaKandidatInteractor.getUserTanyaKandidatList(page, perPage, userId)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it?.questions != null) {
                                if (it.questions.size != 0) {
                                    view?.bindDataTanyaKandidat(it.questions)
                                } else {
                                    view?.showEmptyDataAlert()
                                }
                            } else {
                                view?.showFailedGetDataAlert()
                            }
                        } else {
                            view?.showFailedGetDataAlert()
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showFailedGetDataAlert()
                        view?.showError(it)
                    }
                )
        )
    }
}