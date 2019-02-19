package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.Profile

interface TanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>)
    fun showBanner(bannerInfo: BannerInfo)
    fun showEmptyDataAlert()
    fun showFailedGetDataAlert()
    fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>)
    fun showEmptyNextDataAlert()
    fun onItemUpVoted(position: Int)
    fun onFailedUpVoteItem(liked: Boolean, position: Int)
    fun showItemReportedAlert()
    fun showFailedReportItem()
    fun showFailedDeleteItemAlert()
    fun onItemDeleted(position: Int)
    fun bindUserData(profile: Profile)
}