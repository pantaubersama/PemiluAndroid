package com.pantaubersama.app.ui.menguji.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.model.debat.DebatDetail
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.PantauConstants
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class MengujiPresenter @Inject constructor(
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<MengujiView>() {

    private val isPublik: Boolean
        get() = view?.isPublik ?: true

    fun getBanner() {
        view?.showLoading()
        disposables += bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_DEBAT)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showBanner(it)
            }, {
                view?.showError(it)
                view?.hideBanner()
            })
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
            DebatItem.Challenge(DebatDetail("Raja Kampreta", "", "ekonomi"), 0, DebatItem.Challenge.Status.OPEN),
            DebatItem.Challenge(DebatDetail("Ratu CebonganYK", "", "ekonomi"), 1, DebatItem.Challenge.Status.OPEN),
            DebatItem.Challenge(DebatDetail("Ratu CebonganYK", "", "ekonomi"), 2, DebatItem.Challenge.Status.OPEN))
        view?.showDebatOpen(debatList)
    }
}
