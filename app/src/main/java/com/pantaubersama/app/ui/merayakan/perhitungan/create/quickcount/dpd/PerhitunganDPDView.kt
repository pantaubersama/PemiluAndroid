package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.Dapil
import com.pantaubersama.app.data.model.tps.candidate.Candidate

interface PerhitunganDPDView : BaseView {
    fun bindDapilData(dapil: Dapil)
    fun showEmptyRealCountList()
    fun showGetRealCountListFailedAlert()
    fun showGetDapilFailedAlert()
    fun bindCandidates(candidates: MutableList<Candidate>)
}