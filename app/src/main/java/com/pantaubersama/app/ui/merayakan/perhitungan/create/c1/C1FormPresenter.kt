package com.pantaubersama.app.ui.merayakan.perhitungan.create.c1

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.C1Interactor
import javax.inject.Inject

class C1FormPresenter @Inject constructor(private val c1Interactor: C1Interactor) : BasePresenter<C1FormView>() {
    fun saveSection1(
        tpsId: String,
        a3L: Long,
        a3P: Long,
        a4L: Long,
        a4P: Long,
        aDpkL: Long,
        aDpkP: Long,
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

    fun getC1Data(tpsId: String, c1Type: String, tpsStatus: String) {
        if (tpsStatus != "published") {
            c1Interactor.getC1(tpsId, c1Type)?.let {
                view?.bindC1Data(it)
            }
        } else {
            view?.showLoading()
            disposables.add(
                c1Interactor.getC1Remote(tpsId, c1Type)
                    .subscribe(
                        {
                            view?.bindC1Data(it)
                            view?.dismissLoading()
                        },
                        {
                            view?.showError(it)
                            view?.showFailedGetC1Alert(it.message)
                            view?.dismissLoading()
                        }
                    )
            )
        }
    }

    fun saveSection2(
        tpsId: String,
        dptC7L: Long,
        dptC7P: Long,
        dptBC7L: Long,
        dptBC7P: Long,
        dpkC7L: Long,
        dpkC7P: Long,
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

    fun saveDisabilitas(tpsId: String, disabilitasL: Long, disabilitasP: Long, c1Type: String) {
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

    fun saveHakPilihDisabilitas(tpsId: String, disabilitasL: Long, disabilitasP: Long, c1Type: String) {
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

    fun saveSuratSuaraSection(tpsId: String, reject: Long, unused: Long, used: Long, c1Type: String) {
        disposables.add(
            c1Interactor.saveSuratSuaraSection(
                tpsId,
                reject,
                unused,
                used,
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