package com.pantaubersama.app.ui.penpol.kuis.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.kuis.KuisListItem

interface KuisView : BaseView {
    fun showBanner(bannerInfo: BannerInfo)
    fun hideBanner()
    fun showFailedGetData()
}