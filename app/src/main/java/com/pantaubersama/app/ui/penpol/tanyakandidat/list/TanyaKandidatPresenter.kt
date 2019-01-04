package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import javax.inject.Inject

class TanyaKandidatPresenter @Inject constructor(
    private val tanyaKandidatInteractor: TanyaKandidatInteractor
) : BasePresenter<TanyaKandidatView>() {
    fun isBannerShown() {
        if (!tanyaKandidatInteractor.isBannerShown()!!) {
            view?.showBanner()
        }
    }

    fun getTanyaKandidatList(
        page: Int?,
        perPage: Int?
    ) {
        if (page == 1) {
            view?.showLoading()
        }
        view?.setIsLoading(true)
        disposables?.add(
            tanyaKandidatInteractor
                .getTanyaKandidatlist(
                    page,
                    perPage
            )
            .subscribe(
                {
                    view?.setIsLoading(false)
                    if (page == 1) {
                        view?.dismissLoading()
                        if (it.data?.questions != null) {
                            if (it.data?.questions?.size != 0) {
                                view?.bindDataTanyaKandidat(it.data?.questions!!)
                                if (it.data?.questions?.size!! < perPage!!) {
                                    view?.setDataEnd(true)
                                } else {
                                    view?.setDataEnd(false)
                                }
                            } else {
                                view?.setDataEnd(true)
                                view?.showEmptyDataAlert()
                            }
                        } else {
                            view?.showFailedGetDataAlert()
                        }
                    } else {
                        if (it.data?.questions != null) {
                            if (it.data?.questions?.size != 0) {
                                view?.bindNextDataTanyaKandidat(it.data?.questions!!)
                                if (it.data?.questions?.size!! < perPage!!) {
                                    view?.setDataEnd(true)
                                } else {
                                    view?.setDataEnd(false)
                                }
                            } else {
                                view?.setDataEnd(true)
                                view?.showEmptyNextDataAlert()
                            }
                        } else {
                            view?.showFailedGetDataAlert()
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

    fun upVoteQuestion(id: String?, questionCalass: String?, isLiked: Boolean?, position: Int?) {
        disposables?.add(
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

    fun reportQuestion(id: String?, className: String?) {
        disposables?.add(
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

    fun deleteItem(id: String?, position: Int?) {
        disposables?.add(
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
}