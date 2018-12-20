package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat

interface TanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(tanyaKandidatList: MutableList<TanyaKandidat>?)

}