package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan

interface TanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>)
    fun showBanner()
    fun hideBanner()
    fun showEmptyDataAlert()
    fun showFailedGetDataAlert()
    fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>)
    fun showEmptyNextDataAlert()
    fun setDataEnd(isDataEnd: Boolean)
    fun setIsLoading(isLoading: Boolean)
}