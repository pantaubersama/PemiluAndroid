package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.candidate.CandidateData

interface PerhitunganDPRView : BaseView {
    fun bindCandidates(data: MutableList<CandidateData>)
    fun showGetRealCountListFailedAlert()
    fun showEmptyRealCountList()
}