package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.utils.State

interface MengujiView : BaseView {

    val isPublik: Boolean

    fun showBanner(bannerInfo: BannerInfo)
    fun hideBanner()
    fun showDebatLive(list: List<Challenge>)
    fun showDebatComingSoon(list: List<Challenge>)
    fun showDebatDone(list: List<Challenge>)
    fun showChallengeOngoing(state: State, list: List<Challenge>, hasMore: Boolean)
}