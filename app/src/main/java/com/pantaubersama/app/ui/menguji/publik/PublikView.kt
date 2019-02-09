package com.pantaubersama.app.ui.menguji.publik

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo

interface PublikView : BaseView {
    fun showBanner(bannerInfo: BannerInfo)
}