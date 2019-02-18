package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdprovinsi

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.partai.PoliticalParty

interface PerhitunganDPRDProvinsiView : BaseView {
    fun bindData(parties: MutableList<PoliticalParty>)
}