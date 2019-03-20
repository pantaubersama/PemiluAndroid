package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.rekapitulasi.TotalParticipantData

interface RekapitulasiView : BaseView {
    fun showBanner(it: BannerInfo)
    fun showFailedGetBannerAlert()
    fun bindRekapitulasiNasional(data: Percentage)
    fun showFailedGetTotalParticipantAlert()
    fun bindTotalParticipantData(totalParticipantData: TotalParticipantData)
    fun showFailedGetRekapitulasiNasionalAlert()
}