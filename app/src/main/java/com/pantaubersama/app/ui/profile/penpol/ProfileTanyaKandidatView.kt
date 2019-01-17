package com.pantaubersama.app.ui.profile.penpol

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.Profile

interface ProfileTanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>?)
    fun showEmptyDataAlert()
    fun showFailedGetDataAlert()
    fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>)
    fun showEmptyNextDataAlert()
    fun onItemUpVoted()
    fun onFailedUpVoteItem(liked: Boolean?, position: Int?)
    fun showItemReportedAlert()
    fun showFailedReportItem()
    fun showFailedDeleteItemAlert()
    fun onItemDeleted(position: Int?)
    fun bindProfile(profile: Profile)
}