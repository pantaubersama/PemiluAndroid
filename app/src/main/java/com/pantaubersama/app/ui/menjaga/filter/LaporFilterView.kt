package com.pantaubersama.app.ui.menjaga.filter

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.partai.PoliticalParty

interface LaporFilterView : BaseView {
    fun bindLaporUserFilter(userFilter: String)
    fun bindLaporPartyFilter(partyFilter: PoliticalParty?)
    fun onSuccessSaveFilter()
}