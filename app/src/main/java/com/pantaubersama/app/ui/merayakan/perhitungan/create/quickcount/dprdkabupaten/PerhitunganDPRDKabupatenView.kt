package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdkabupaten

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.partai.PoliticalParty

interface PerhitunganDPRDKabupatenView : BaseView {
    fun bindData(parties: MutableList<PoliticalParty>)
}