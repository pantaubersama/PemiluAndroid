package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TanyaKandidateInteractor
import javax.inject.Inject

class TanyaKandidatPresenter @Inject constructor(
    private val tanyaKandidateInteractor: TanyaKandidateInteractor
) : BasePresenter<TanyaKandidatView>() {
    fun isBannerShown() {
        if (!tanyaKandidateInteractor.isBannerShown()!!) {
            view?.showBanner()
        }
    }

    fun getTanyaKandidatList(
        page: Int?,
        perPage: Int?,
        orderBy: String?,
        direction: String?,
        filterBy: String?
    ) {
        if (page == 1) {
            view?.showLoading()
        }
        view?.setIsLoading(true)
        disposables?.add(tanyaKandidateInteractor.getTanyaKandidatlist(
            page,
            perPage,
            orderBy,
            direction,
            filterBy)
            .doOnSuccess {
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
            }
            .doOnError {
                view?.dismissLoading()
                view?.showFailedGetDataAlert()
                view?.showError(it)
            }
            .subscribe())
    }
}