package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.debat.DebatDetail
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.PantauConstants.Debat.Title

class DebatListPresenter : BasePresenter<DebatListView>() {

    fun getDebatItems(title: String) {
        when (title) {
            Title.PUBLIK_LIVE_NOW -> getDebatLive()
            Title.PUBLIK_COMING_SOON, Title.PERSONAL_COMING_SOON -> getDebatComingSoon()
            Title.PUBLIK_DONE, Title.PERSONAL_DONE -> getDebatDone()
            Title.PUBLIK_CHALLENGE, Title.PERSONAL_CHALLENGE_IN_PROGRESS, Title.PERSONAL_CHALLENGE -> getDebatOpen()
        }
    }

    fun getDebatLive() {
        val debatList = (0..9).map {
            DebatItem.LiveNow(DebatDetail("Ratu CebonganYK", "Raja Kampreta", "ekonomi"))
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatComingSoon() {
        val debatList = (0..9).map {
            DebatItem.ComingSoon(DebatDetail("Ratu CebonganYK", "Raja Kampreta", "ekonomi"),
                "24 Maret 2019", "16:00 - 17:00")
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatDone() {
        val debatList = (0..9).map {
            DebatItem.Done(DebatDetail("Ratu CebonganYK", "Raja Kampreta", "ekonomi"),
                70, 70, 50)
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatOpen() {
        var debatList = listOf(
            DebatItem.Open(DebatDetail("Raja Kampreta", "", "ekonomi"), 0, false),
            DebatItem.Open(DebatDetail("Ratu CebonganYK", "", "ekonomi"), 1, true),
            DebatItem.Open(DebatDetail("Ratu CebonganYK", "", "ekonomi"), 2, true))
        debatList += debatList + debatList
        view?.showDebatItems(debatList)
    }
}
