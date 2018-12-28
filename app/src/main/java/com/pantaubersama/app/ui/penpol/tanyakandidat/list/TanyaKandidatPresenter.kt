package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TanyaKandidateInteractor
import timber.log.Timber
import javax.inject.Inject

class TanyaKandidatPresenter @Inject constructor(
    private val tanyaKandidateInteractor: TanyaKandidateInteractor
) : BasePresenter<TanyaKandidatView>() {
    fun getTanyaKandidatList(
        page: Int?,
        perPage: Int?,
        orderBy: String?,
        direction: String?,
        filterBy: String?
    ) {
        view?.showLoading()
        disposables?.add(tanyaKandidateInteractor.getTanyaKandidatlist(
            page,
            perPage,
            orderBy,
            direction,
            filterBy)
            .doOnSuccess {
                Timber.d(it.toString())
                view?.dismissLoading()
                if (it.data.questions != null) {
                    if (it.data.questions?.size != 0) {
                        view?.bindDataTanyaKandidat(it.data.questions)
                    } else {
                        view?.showEmptyDataAlert()
                    }
                } else {
                    view?.showFailedGetDataAlert()
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