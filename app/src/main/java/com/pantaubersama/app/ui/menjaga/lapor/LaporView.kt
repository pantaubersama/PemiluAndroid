package com.pantaubersama.app.ui.menjaga.lapor

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.user.Profile

interface LaporView : BaseView {
    fun bindProfile(profile: Profile)
    fun showBanner(banner: BannerInfo)
    fun showFailedGetDataAlert()
}