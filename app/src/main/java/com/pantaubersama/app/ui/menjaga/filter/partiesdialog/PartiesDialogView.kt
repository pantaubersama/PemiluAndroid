package com.pantaubersama.app.ui.menjaga.filter.partiesdialog

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.partai.PoliticalParty

interface PartiesDialogView : BaseView {
    fun showPartai(parties: MutableList<PoliticalParty>)
}