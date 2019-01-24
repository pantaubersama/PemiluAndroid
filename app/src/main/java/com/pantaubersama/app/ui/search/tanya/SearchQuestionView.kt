package com.pantaubersama.app.ui.search.tanya

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.Profile

interface SearchQuestionView : BaseView {
    fun bindProfile(profile: Profile)
    fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>)
    fun showEmptyDataAlert()
    fun showFailedGetDataAlert()
    fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>)
    fun showEmptyNextDataAlert()
    fun onItemUpVoted()
    fun onFailedUpVoteItem(liked: Boolean, position: Int)
    fun showItemReportedAlert()
    fun showFailedReportItem()
    fun showFailedDeleteItemAlert()
    fun onItemDeleted(position: Int)
    fun setDataEnd()
}