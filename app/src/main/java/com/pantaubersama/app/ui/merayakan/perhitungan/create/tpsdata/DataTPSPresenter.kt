package com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TPSInteractor
import javax.inject.Inject

class DataTPSPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) : BasePresenter<DataTPSView>() {
    fun getProvincesData() {
        view?.showProvincesLoading()
        disposables.add(
            tpsInteractor.getProvinces()
                .subscribe(
                    {
                        view?.dismissProvincesLoading()
                        view?.bindProvincesToSpinner(it)
                    },
                    {
                        view?.dismissProvincesLoading()
                        view?.showFailedGetProvincesAlert()
                        view?.showError(it)
                    }
                )
        )
    }
}