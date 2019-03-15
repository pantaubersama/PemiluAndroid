package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RealCountInteractor
import com.pantaubersama.app.data.model.tps.TPS
import javax.inject.Inject

class PerhitunganDPDPresenter @Inject constructor(
    private val realCountInteractor: RealCountInteractor
) : BasePresenter<PerhitunganDPDView>() {
    fun getDPDData(tps: TPS, realCountType: String) {
        view?.showLoading()
        disposables.add(
            realCountInteractor.getDapil(tps, realCountType)
                .subscribe(
                    { dapil ->
                        view?.bindDapilData(dapil)
                        disposables.add(
                            realCountInteractor.getRealCountList(dapil.id, realCountType)
                                .subscribe(
                                    {
                                        if (it.size != 0) {
                                            view?.dismissLoading()
                                            view?.bindCandidates(it[0].candidates)
                                        } else {
                                            view?.showEmptyRealCountList()
                                        }
                                    },
                                    {
                                        view?.dismissLoading()
                                        view?.showError(it)
                                        view?.showGetRealCountListFailedAlert()
                                    }
                                )
                        )
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showGetDapilFailedAlert()
                    }
                )
        )
    }
}