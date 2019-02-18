package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.partai.PoliticalParty

interface PerhitunganDPRRIView : BaseView {
    fun bindData(parties: MutableList<PoliticalParty>)
}