package com.pantaubersama.app.ui.merayakan.perhitungan.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tps.TPSData
import com.pantaubersama.app.data.model.user.Profile

interface PerhitunganView : BaseView {
    fun bindProfile(profile: Profile)
    fun bindPerhitungan(tpses: MutableList<TPSData>)
    fun showFailedGetDataAlert()
    fun showBanner(bannerInfo: BannerInfo)
}