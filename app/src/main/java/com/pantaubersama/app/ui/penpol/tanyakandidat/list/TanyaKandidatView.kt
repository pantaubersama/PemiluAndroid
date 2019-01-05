package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan

interface TanyaKandidatView : BaseView {
    fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>)
    fun showBanner(bannerInfo: BannerInfo)
    fun hideBanner()
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
}