package com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TPSInteractor
import javax.inject.Inject

class TPSListPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) : BasePresenter<TPSListView>() {
    fun getTPSListData(page: Int, perPage: Int, villageCode: Long) {
        if (page == 1) {
            view?.showLoading()
        }
        disposables.add(
            tpsInteractor.getTpses(page, perPage, villageCode)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it.size != 0) {
                                view?.bindTpses(it)
                                if (it.size < perPage) {
                                    view?.setDataEnd()
                                }
                            } else {
                                view?.showEmptyDataAlert()
                            }
                        } else {
                            if (it.size != 0) {
                                view?.bindNextTpses(it)
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
                        view?.showError(it)
                        view?.showFailedLoadDataAlert()
                    }
                )
        )
    }
}