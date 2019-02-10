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

    fun getDebatLive() {
        val debatList = (0..3).map {
            DebatItem.LiveNow(DebatDetail("Ratu CebonganYK", "Raja Kampreta", "ekonomi"))
        }
        view?.showDebatLive(debatList)
    }

    fun getDebatComingSoon() {
        val debatList = (0..2).map {
            DebatItem.ComingSoon(DebatDetail("Ratu CebonganYK", "Raja Kampreta", "ekonomi"),
                "24 Maret 2019", "16:00 - 17:00")
        }
        view?.showDebatComingSoon(debatList)
    }

    fun getDebatDone() {
        val debatList = (0..2).map {
            DebatItem.Done(DebatDetail("Ratu CebonganYK", "Raja Kampreta", "ekonomi"),
                70, 70, 50)
        }
        view?.showDebatDone(debatList)
    }

    fun getDebatOpen() {
        val debatList = listOf(
            DebatItem.Open(DebatDetail("Raja Kampreta", "", "ekonomi"), 0, false),
            DebatItem.Open(DebatDetail("Ratu CebonganYK", "", "ekonomi"), 0, true),
            DebatItem.Open(DebatDetail("Ratu CebonganYK", "", "ekonomi"), 1, true))
        view?.showDebatOpen(debatList)
    }

}
