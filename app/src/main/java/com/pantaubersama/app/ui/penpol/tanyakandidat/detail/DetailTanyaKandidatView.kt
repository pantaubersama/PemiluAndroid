package com.pantaubersama.app.ui.penpol.tanyakandidat.detail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan

interface DetailTanyaKandidatView : BaseView {
    fun bindData(question: Pertanyaan)
    fun onFailedGetData(throwable: Throwable)
    fun onItemUpVoted()
    fun onFailedUpVoteItem(liked: Boolean)
    fun showItemReportedAlert()
    fun showFailedReportItem()
    fun showFailedDeleteItemAlert()
    fun onItemDeleted()
    fun onClickCopyUrl()
    fun onClickShare()
    fun onClickLapor()
    fun onClickDeleteItem()
}