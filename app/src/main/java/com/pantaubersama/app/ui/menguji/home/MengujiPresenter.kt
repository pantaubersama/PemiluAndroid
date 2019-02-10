package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.DebatDetail
import com.pantaubersama.app.data.model.debat.DebatItem
import javax.inject.Inject

class MengujiPresenter @Inject constructor() : BasePresenter<MengujiView>() {

    private val isPublik: Boolean
        get() = view?.isPublik ?: true

    fun getBanner() {
        val body = if (isPublik)
            "Chivalry over Bigotry\nExplore WordStadium. Kamu bisa cari tantangan terbuka dan debat yang akan atau sudah berlangsung."
        else
            "Be truthful and gentle.\nUtarakan pernyataan kamu mengenai sebuah bidang kajian. Buat tantangan dan undang orang untuk berdebat denganmu!"

        view?.showBanner(BannerInfo(title = "Menguji", body = body))
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
