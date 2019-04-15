package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpr

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
                        if (it.data != null) {
                            view?.bindDapilData(it.data)
                            disposables.add(
                                realCountInteractor.getRealCountList(it.data.id, realCountType)
                                    .subscribe(
                                        {
                                            if (it.size != 0 ) {
                                                view?.dismissLoading()
                                                view?.bindCandidates(it)
                                            } else {
                                                view?.showEmptyData()
                                            }
                                        },
                                        {
                                            view?.dismissLoading()
                                            view?.showError(it)
                                            view?.showGetRealCountListFailedAlert()
                                        }
                                    )
                            )
                        } else {
                            view?.showEmptyData()
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showGetDapilFailedAlert()
                    }
                )
        )
    }

    fun getRealCount(tpsId: String, realCountType: String, tpsStatus: String) {
        when (tpsStatus) {
            "published" -> {
                disposables.add(
                    realCountInteractor.getApiRealCount(tpsId, realCountType)
                        .subscribe(
                            {
                                view?.dismissLoading()
                                view?.bindRealCount(it)
                            },
                            {
                                view?.dismissLoading()
                                view?.showError(it)
                                view?.showFailedGetRealCountAlert(it.message)
                            }
                        )
                )
            }
            "local" -> realCountInteractor.getRealCount(tpsId, realCountType)?.let {
                view?.bindRealCount(it)
            }
            else -> realCountInteractor.getRealCount(tpsId, realCountType)?.let {
                view?.bindRealCount(it)
            }
        }
    }

    fun saveRealCount(tpsId: String, realCountType: String, items: MutableList<CandidateData>, invalidVote: Long? = null) {
        disposables.add(
            realCountInteractor.saveDprRealCount(tpsId, realCountType, items, invalidVote)
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