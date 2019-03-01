package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import com.pantaubersama.app.base.BasePresenter
import javax.inject.Inject

class PerhitunganPresidenPresenter @Inject constructor() : BasePresenter<PerhitunganPresidenView>() {
    var candidate1Count = 0
    var candidate2Count = 0
    var validCount = 0
    var invalidCount = 0
    var allCount = 0
    fun saveCandidate1Count(candidate1Count: Int) {
        this.candidate1Count = candidate1Count
        validCount = this.candidate1Count + this.candidate2Count
        allCount = this.invalidCount + this.validCount
        view?.onSuccessVoteCandidateCount(validCount, invalidCount, allCount)
    }

    fun saveCandidate2Count(candidate2Count: Int) {
        this.candidate2Count = candidate2Count
        validCount = this.candidate1Count + this.candidate2Count
        allCount = this.invalidCount + this.validCount
        view?.onSuccessVoteCandidateCount(validCount, invalidCount, allCount)
    }

    fun saveInvalidVoteCount(invalidCount: Int) {
        this.invalidCount = invalidCount
        allCount = this.invalidCount + this.validCount
        view?.onSuccessVoteCandidateCount(validCount, invalidCount, allCount)
    }
}