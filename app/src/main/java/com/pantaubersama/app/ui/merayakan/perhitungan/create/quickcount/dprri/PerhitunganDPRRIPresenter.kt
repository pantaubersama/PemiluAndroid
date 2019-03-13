package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RealCountInteractor
import com.pantaubersama.app.data.model.tps.TPS
import javax.inject.Inject

class PerhitunganDPRRIPresenter @Inject constructor(
    private val realCountInteractor: RealCountInteractor
) : BasePresenter<PerhitunganDPRRIView>() {
    fun getRealCountList(tps: TPS, realCountType: String) {
        view?.showLoading()
        disposables.add(
            realCountInteractor.getRealCountList(tps, realCountType)
                .subscribe(
                    {
                        if (it.size != 0) {
                            view?.dismissLoading()
                            view?.bindCandidates(it)
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
    }
}