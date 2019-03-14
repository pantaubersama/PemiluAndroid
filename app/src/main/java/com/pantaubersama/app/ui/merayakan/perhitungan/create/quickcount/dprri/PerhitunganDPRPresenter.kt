package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RealCountInteractor
import com.pantaubersama.app.data.model.tps.TPS
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

    fun saveRealCountParty(tpsId: String, partyId: Int, realCountType: String, partyCount: Int) {
        disposables.add(
            realCountInteractor.saveRealCountParty(tpsId, partyId, realCountType, partyCount)
                .subscribe(
                    {
                        view?.onSuccessSavePartyRealCount()
                    },
                    {
                        view?.showError(it)
                    }
                )
        )
    }

    fun getRealCount(tpsId: String, realCountType: String) {
        realCountInteractor.getRealCount(tpsId, realCountType)?.let {
            view?.bindRealCount(it)
        }
    }

    fun saveRealCountCandidate(tpsId: String, candidateId: Int, partyId: Int, realCountType: String, totalCount: Int) {
        disposables.add(
            realCountInteractor.saveRealCountCandidate(tpsId, candidateId, partyId, realCountType, totalCount)
                .subscribe(
                    {
                        view?.onSuccessSaveCandidateRealCount()
                    },
                    {
                        view?.showError(it)
                    }
                )
        )
    }
}