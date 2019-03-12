package com.pantaubersama.app.ui.merayakan.perhitungan.create.c1

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.C1Interactor
import javax.inject.Inject

class C1FormPresenter @Inject constructor(private val c1Interactor: C1Interactor) : BasePresenter<C1FormView>() {
    fun saveSection1(
        tpsId: String,
        a3L: Int,
        a3P: Int,
        a4L: Int,
        a4P: Int,
        aDpkL: Int,
        aDpkP: Int,
        c1Type: String
    ) {
        disposables.add(
            c1Interactor.saveSection1(
                tpsId,
                a3L,
                a3P,
                a4L,
                a4P,
                aDpkL,
                aDpkP,
                c1Type
            )
                .subscribe(
                    {
                        view?.onSuccessSaveData()
                    },
                    {
                        view?.showError(it)
                        view?.showFailedSaveDataAlert()
                    }
                )
        )
    }

    fun getC1Data(tpsId: String, c1Type: String) {
        c1Interactor.getC1(tpsId, c1Type)?.let {
            view?.bindC1Data(it)
        }
    }
}