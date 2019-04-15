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
                    {
                        if (it.data != null) {
                            view?.bindDapilData(it.data)
                            disposables.add(
                                realCountInteractor.getRealCountList(it.data.id, realCountType)
                                    .subscribe(
                                        {
                                            if (it.size != 0) {
                                                view?.dismissLoading()
                                                view?.bindCandidates(it[0].candidates)
                                            } else {
                                                view?.showEmptyRealCountList()
                                            }
                                        },
                                        { t ->
                                            view?.dismissLoading()
                                            view?.showError(t)
                                        }
                                    )
                            )
                        } else {
                            view?.showGetDapilFailedAlert()
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

    fun saveRealCount(tpsId: String, realCountType: String, candidates: MutableList<Candidate>, invalidVote: Long? = null) {
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

    fun getRealCount(tpsId: String, realCountType: String, tpsStatus: String) {
        when (tpsStatus) {
            "published" -> {
                view?.showLoading()
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
            "local" -> realCountInteractor.getRealCount(tpsId, realCountType)?.let { view?.bindRealCount(it) }
            else -> realCountInteractor.getRealCount(tpsId, realCountType)?.let { view?.bindRealCount(it) }
        }
    }
}