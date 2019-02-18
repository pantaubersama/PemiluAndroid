package com.pantaubersama.app.ui.merayakan.perhitungan.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.tps.TPSData
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class PerhitunganPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<PerhitunganView>() {
    fun getProfile() {
        view?.bindProfile(profileInteractor.getProfile())
    }

    fun getPerhitunganData(page: Int, perPage: Int) {
//        view?.showLoading()
        val tpses: MutableList<TPSData> = ArrayList()
        tpses.add(
            TPSData("TPS 001", "DIY", "Sleman", "Moyudan", "Sumberagung", 0)
        )
        tpses.add(
            TPSData("TPS 002", "DIY", "Sleman", "Moyudan", "Sumberagung", 1)
        )
        tpses.add(
            TPSData("TPS 003", "DIY", "Sleman", "Moyudan", "Sumberagung", 2)
        )
        view?.dismissLoading()
        view?.bindPerhitungan(tpses)
    }

    fun getBanner() {
        view?.showLoading()
        disposables.add(
            bannerInfoInteractor.getBannerInfo(PantauConstants.PERHITUNGAN)
                .subscribe(
                    {
                        view?.showBanner(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetDataAlert()
                    }
                )
        )
    }
}