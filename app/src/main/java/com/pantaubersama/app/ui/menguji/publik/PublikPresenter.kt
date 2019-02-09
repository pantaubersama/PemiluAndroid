package com.pantaubersama.app.ui.menguji.publik

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import javax.inject.Inject

class PublikPresenter @Inject constructor() : BasePresenter<PublikView>() {

    fun getBanner() {
        view?.showBanner(BannerInfo(title = "Menguji", body = "Explore WordStadium. Kamu bisa cari tantangan terbuka dan debat yang akan atau sudah berlangsung."))
    }

}
