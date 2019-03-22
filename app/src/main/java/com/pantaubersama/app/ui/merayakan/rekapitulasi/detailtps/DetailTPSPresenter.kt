package com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RekapitulasiInteractor
import javax.inject.Inject

class DetailTPSPresenter @Inject constructor(
    private val rekapitulasiInteractor: RekapitulasiInteractor
) : BasePresenter<DetailTPSView>() {
    fun getRekapitulasi(tpsId: String, villageCode: Long, tpsNumber: Int) {
        view?.dismissLoading()
        disposables.add(
            rekapitulasiInteractor.getRekapitulasiDetail(tpsId, villageCode, tpsNumber)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.bindRealCount(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetRealCountAlert()
                    }
                )
        )
    }
}