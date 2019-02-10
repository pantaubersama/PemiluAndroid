package com.pantaubersama.app.ui.menguji.publik

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.DebatDetail
import com.pantaubersama.app.data.model.debat.DebatItem
import javax.inject.Inject

class PublikPresenter @Inject constructor() : BasePresenter<PublikView>() {

    fun getBanner() {
        view?.showBanner(BannerInfo(title = "Menguji", body = "Explore WordStadium. Kamu bisa cari tantangan terbuka dan debat yang akan atau sudah berlangsung."))
    }

    fun getLiveDebat() {
        val debatList = (0..3).map {
            DebatItem.LiveNow(DebatDetail("Ratu CebonganYK", "Raja Kampreta", "ekonomi"))
        }
        view?.showLiveDebat(debatList)
    }

}
