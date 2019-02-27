package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.State

interface MengujiView : BaseView {

    val isPublik: Boolean

    fun showBanner(bannerInfo: BannerInfo)
    fun hideBanner()
    fun showDebatLive(list: List<DebatItem.LiveNow>)
    fun showDebatComingSoon(list: List<DebatItem.ComingSoon>)
    fun showDebatDone(list: List<DebatItem.Done>)
    fun showDebatOpen(state: State, list: List<DebatItem.Challenge>, hasMore: Boolean)
}