package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.utils.State

interface MengujiView : BaseView {

    val isPublik: Boolean

    fun showBanner(bannerInfo: BannerInfo)
    fun hideBanner()
    fun showChallengeLive(state: State, list: List<Challenge>, hasMore: Boolean)
    fun showChallengeComingSoon(state: State, list: List<Challenge>, hasMore: Boolean)
    fun showChallengeDone(state: State, list: List<Challenge>, hasMore: Boolean)
    fun showChallengeOngoing(state: State, list: List<Challenge>, hasMore: Boolean)
}