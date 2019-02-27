package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.debat.DebatDetail
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.data.model.debat.DUMMY_CHALLENGER
import com.pantaubersama.app.data.model.debat.DUMMY_OPPONENT
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
            DebatItem.LiveNow(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", ""))
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatComingSoon() {
        val debatList = (0..9).map {
            DebatItem.ComingSoon(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", ""),
                "24 Maret 2019", "16:00 - 17:00")
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatDone() {
        val debatList = (0..9).map {
            DebatItem.Done(DebatDetail(DUMMY_CHALLENGER, DUMMY_OPPONENT, "ekonomi", ""),
                70, 70, 50)
        }
        view?.showDebatItems(debatList)
    }

    fun getDebatOpen() {
        var debatList = listOf(
            DebatItem.Challenge(DebatDetail(DUMMY_OPPONENT, null, "ekonomi", ""), 0,
                null, DebatItem.Challenge.Status.OPEN),
            DebatItem.Challenge(DebatDetail(DUMMY_CHALLENGER, null, "ekonomi", ""), 1,
                null, DebatItem.Challenge.Status.OPEN),
            DebatItem.Challenge(DebatDetail(DUMMY_CHALLENGER, null, "ekonomi", ""), 2,
                null, DebatItem.Challenge.Status.OPEN),
            DebatItem.Challenge(DebatDetail(DUMMY_CHALLENGER, null, "ekonomi", ""), 1,
                null, DebatItem.Challenge.Status.DIRECT),
            DebatItem.Challenge(DebatDetail(DUMMY_CHALLENGER, null, "ekonomi", ""), 0,
                null, DebatItem.Challenge.Status.DENIED),
            DebatItem.Challenge(DebatDetail(DUMMY_CHALLENGER, null, "ekonomi", ""), 0,
                null, DebatItem.Challenge.Status.EXPIRED))
        debatList += debatList
        view?.showDebatItems(debatList)
    }
}
