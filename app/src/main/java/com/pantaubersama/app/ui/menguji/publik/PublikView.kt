package com.pantaubersama.app.ui.menguji.publik

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.DebatItem

interface PublikView : BaseView {
    fun showBanner(bannerInfo: BannerInfo)
    fun showLiveDebat(list: List<DebatItem.LiveNow>)
}