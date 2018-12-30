package com.pantaubersama.app.ui.profile.penpol

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan

interface ProfileTanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>?)
    fun showEmptyDataAlert()
}