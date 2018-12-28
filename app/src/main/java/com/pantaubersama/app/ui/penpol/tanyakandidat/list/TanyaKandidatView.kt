package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan

interface TanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>?)
    fun showEmptyDataAlert()
    fun showFailedGetDataAlert()
}