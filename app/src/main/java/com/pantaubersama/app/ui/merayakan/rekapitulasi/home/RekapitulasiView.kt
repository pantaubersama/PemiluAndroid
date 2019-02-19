package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData

interface RekapitulasiView : BaseView {
    fun showBanner(it: BannerInfo)
    fun showFailedGetBannerAlert()
    fun bindRekapitulasi(data: MutableList<RekapitulasiData>)
}