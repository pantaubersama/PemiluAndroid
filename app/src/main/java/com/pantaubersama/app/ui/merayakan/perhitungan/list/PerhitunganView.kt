package com.pantaubersama.app.ui.merayakan.perhitungan.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.user.Profile

interface PerhitunganView : BaseView {
    fun bindProfile(profile: Profile)
    fun bindTPSes(tpses: MutableList<TPS>)
    fun showFailedGetDataAlert()
    fun showBanner(bannerInfo: BannerInfo)
    fun showEmptyAlert()
    fun showFailedDeleteItemAlert()
    fun showDeleteItemLoading()
    fun dismissDeleteItemLoading()
    fun onSuccessDeleteItem(position: Int)
    fun onSuccessCreateSandboxTps()
    fun onFailureCreateSandboxTps()
}