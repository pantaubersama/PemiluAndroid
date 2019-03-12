package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RealCountInteractor
import javax.inject.Inject

class PerhitunganPresidenPresenter @Inject constructor(
    private val realCountInteractor: RealCountInteractor
) : BasePresenter<PerhitunganPresidenView>() {
    fun saveCandidate1Count(candidate1Count: Int, tpsId: String) {
        disposables.add(
            realCountInteractor.savePresidentCandidate1Count(candidate1Count, tpsId)
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

    fun saveCandidate2Count(candidate2Count: Int, tpsId: String) {
        disposables.add(
            realCountInteractor.savePresidentCandidate2Count(candidate2Count, tpsId)
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

    fun saveInvalidVoteCount(invalidCount: Int, tpsId: String) {
        disposables.add(
            realCountInteractor.saveInvalidCount(invalidCount, tpsId)
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

    fun getCounter(tpsId: String) {
        realCountInteractor.getRealCount(tpsId)?.let {
            val validCount = it.candidates[0].totalVote + it.candidates[1].totalVote
            val allCount = it.invalidVote + validCount
            view?.bindCounter(validCount, it.invalidVote, allCount)
        }
    }

    fun getRealCount(tpsId: String) {
        view?.bindRealCount(realCountInteractor.getRealCount(tpsId))
    }
}