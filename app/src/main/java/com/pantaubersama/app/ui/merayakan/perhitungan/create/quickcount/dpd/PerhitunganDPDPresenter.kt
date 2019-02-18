package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.kandidat.CandidateData
import javax.inject.Inject

class PerhitunganDPDPresenter @Inject constructor() : BasePresenter<PerhitunganDPDView>() {
    fun getDPDData() {
        val candidate1: MutableList<CandidateData> = ArrayList()
        candidate1.add(CandidateData("Anwar", 1))
        candidate1.add(CandidateData("Jalu", 2))
        candidate1.add(CandidateData("Supardi B.A", 3))
        candidate1.add(CandidateData("Suryono B.A", 4))
        candidate1.add(CandidateData("Saerah Supandi", 5))
        view?.bindData(candidate1)
    }
}