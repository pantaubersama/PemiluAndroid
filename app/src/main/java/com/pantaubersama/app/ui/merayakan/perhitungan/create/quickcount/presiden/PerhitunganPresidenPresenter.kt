package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RealCountInteractor
import javax.inject.Inject

class PerhitunganPresidenPresenter @Inject constructor(
    private val realCountInteractor: RealCountInteractor
) : BasePresenter<PerhitunganPresidenView>() {
    fun saveCandidateCount(candidate1Count: Long, candidate2Count: Long, invalidCount: Long, tpsId: String, realCountType: String) {
        disposables.add(
            realCountInteractor.savePresidentRealCount(candidate1Count, candidate2Count, invalidCount, tpsId, realCountType)
                .subscribe(
                    {
                        view?.onSuccessVoteCandidateCount()
                    },
                    {
                        view?.showError(it)
                        view?.showFailedSaveDataAlert()
                    }
                )
        )
    }

    fun getRealCount(tpsId: String, tpsStatus: String, realCountType: String) {
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
                                view?.showFailedGetRealCountAlert(it.message)
                                view?.showError(it)
                            }
                        )
                )
            }
            "local" -> view?.bindRealCount(realCountInteractor.getPresidentRealCount(tpsId, realCountType))
            else -> view?.bindRealCount(realCountInteractor.getPresidentRealCount(tpsId, realCountType))
        }
    }
}