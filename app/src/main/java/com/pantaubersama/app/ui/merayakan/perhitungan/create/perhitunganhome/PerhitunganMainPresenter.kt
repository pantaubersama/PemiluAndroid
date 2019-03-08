package com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TPSInteractor
import com.pantaubersama.app.data.model.tps.TPS
import javax.inject.Inject

class PerhitunganMainPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) : BasePresenter<PerhitunganMainView>() {
    fun deletePerhitungan(tps: TPS) {
        view?.showLoading()
        disposables.add(
            tpsInteractor.deleteTps(tps)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.onSuccessDeleteTps()
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedDeleteTpsAlert()
                    }
                )
        )
    }

    fun getTps(tpsId: String) {
        view?.bindTps(tpsInteractor.getTps(tpsId))
    }
}