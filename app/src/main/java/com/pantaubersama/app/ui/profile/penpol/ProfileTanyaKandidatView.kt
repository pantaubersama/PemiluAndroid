package com.pantaubersama.app.ui.profile.penpol

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat

interface ProfileTanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(tanyaKandidatList: MutableList<TanyaKandidat>?)
    fun showEmptyDataAlert()
}