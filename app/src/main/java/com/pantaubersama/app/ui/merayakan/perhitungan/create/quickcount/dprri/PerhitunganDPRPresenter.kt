package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RealCountInteractor
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import javax.inject.Inject

class PerhitunganDPRPresenter @Inject constructor(
    private val realCountInteractor: RealCountInteractor
) : BasePresenter<PerhitunganDPRView>() {
    fun getRealCountList(tps: TPS, realCountType: String) {
        view?.showLoading()
        disposables.add(
            realCountInteractor.getDapil(tps, realCountType)
                .subscribe(
                    {
                        view?.bindDapilData(it)
                        disposables.add(
                            realCountInteractor.getRealCountList(it.id, realCountType)
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
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showGetDapilFailedAlert()
                    }
                )
        )
    }

    fun getRealCount(tpsId: String, realCountType: String) {
        realCountInteractor.getRealCount(tpsId, realCountType)?.let {
            view?.bindRealCount(it)
        }
    }

    fun saveRealCount(tpsId: String, realCountType: String, items: MutableList<CandidateData>, invalidVote: Int? = null) {
        disposables.add(
            realCountInteractor.saveRealCount(tpsId, realCountType, items, invalidVote)
                .subscribe(
                    {
                        view?.onSuccessSaveRealCount()
                    },
                    {
                        view?.showError(it)
                    }
                )
        )
    }
}