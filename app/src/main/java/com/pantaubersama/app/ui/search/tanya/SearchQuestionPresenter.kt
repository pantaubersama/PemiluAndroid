package com.pantaubersama.app.ui.search.tanya

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import javax.inject.Inject

class SearchQuestionPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val tanyaKandidatInteractor: TanyaKandidatInteractor
) : BasePresenter<SearchQuestionView>() {
    fun getProfile() {
        view?.bindProfile(profileInteractor.getProfile())
    }

    fun upVoteQuestion(id: String?, questionCalass: String, isLiked: Boolean, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .upVoteQuestion(
                    id,
                    questionCalass
                )
                .subscribe(
                    {
                        view?.onItemUpVoted(position)
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(isLiked, position)
                    }
                )
        )
    }

    fun reportQuestion(id: String?, className: String?) {
        disposables.add(
            tanyaKandidatInteractor
                .reportQuestion(
                    id,
                    className
                )
                .subscribe(
                    {
                        view?.showItemReportedAlert()
                    },
                    {
                        view?.showError(it)
                        view?.showFailedReportItem()
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
                        view?.onItemUpVoted(position)
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(liked, position)
                    }
                )
        )
    }

    fun searchQuestion(keyword: String, page: Int, perPage: Int) {
        if (page == 1) {
            view?.showLoading()
        }
        disposables.add(
            tanyaKandidatInteractor
                .searchTanyaKandidat(
                    keyword,
                    page,
                    perPage)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it.size != 0) {
                                view?.bindDataTanyaKandidat(it)
                                if (it.size < perPage) {
                                    view?.setDataEnd()
                                }
                            } else {
                                view?.showEmptyDataAlert()
                            }
                        } else {
                            if (it.size != 0) {
                                view?.bindNextDataTanyaKandidat(it)
                                if (it.size < perPage) {
                                    view?.setDataEnd()
                                }
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
                ))
    }
}