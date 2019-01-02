package com.pantaubersama.app.ui.bannerinfo

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo

/**
 * @author edityomurti on 27/12/2018 21:44
 */
interface BannerInfoView : BaseView {
    fun showBannerInfo(item: BannerInfo)
}