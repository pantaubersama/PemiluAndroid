package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kandidat.CandidateData

interface PerhitunganDPDView : BaseView {
    fun bindData(candidates: MutableList<CandidateData>)
}