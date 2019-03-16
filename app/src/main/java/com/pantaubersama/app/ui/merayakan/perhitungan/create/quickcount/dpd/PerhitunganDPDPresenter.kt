package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RealCountInteractor
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.candidate.Candidate
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

    fun saveRealCount(tpsId: String, realCountType: String, candidates: MutableList<Candidate>, invalidVote: Int? = null) {
        disposables.add(
            realCountInteractor.saveDpdRealCount(tpsId, realCountType, candidates, invalidVote)
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

    fun getRealCount(tpsId: String, realCountType: String) {
        realCountInteractor.getRealCount(tpsId, realCountType)?.let { view?.bindRealCount(it) }
    }
}