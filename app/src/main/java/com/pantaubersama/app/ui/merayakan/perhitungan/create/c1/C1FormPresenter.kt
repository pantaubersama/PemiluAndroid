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

    fun saveSection2(
        tpsId: String,
        dptC7L: Int,
        dptC7P: Int,
        dptBC7L: Int,
        dptBC7P: Int,
        dpkC7L: Int,
        dpkC7P: Int,
        c1Type: String
    ) {
        disposables.add(
            c1Interactor.saveSection2(
                tpsId,
                dptC7L,
                dptC7P,
                dptBC7L,
                dptBC7P,
                dpkC7L,
                dpkC7P,
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

    fun saveDisabilitas(tpsId: String, disabilitasL: Int, disabilitasP: Int, c1Type: String) {
        disposables.add(
            c1Interactor.saveDisabilitas(
                tpsId,
                disabilitasL,
                disabilitasP,
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

    fun saveHakPilihDisabilitas(tpsId: String, disabilitasL: Int, disabilitasP: Int, c1Type: String) {
        disposables.add(
            c1Interactor.saveHakPilihDisabilitas(
                tpsId,
                disabilitasL,
                disabilitasP,
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
}