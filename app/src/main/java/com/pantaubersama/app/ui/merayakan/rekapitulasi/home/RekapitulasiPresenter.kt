package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.model.kuis.Team
import com.pantaubersama.app.data.model.kuis.TeamPercentage
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class RekapitulasiPresenter @Inject constructor(
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<RekapitulasiView>() {
    fun getBanner() {
        view?.showLoading()
        disposables.add(
            bannerInfoInteractor.getBannerInfo(PantauConstants.REKAPITULASI)
                .subscribe(
                    {
                        view?.showBanner(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetBannerAlert()
                    }
                )
        )
    }

    fun getRekapitulasiData() {
        val teamsPercentage: MutableList<TeamPercentage> = ArrayList()
        teamsPercentage.add(TeamPercentage(Team(1, "Jokowi-Ma'ruf", ""), 47f, 200000))
        teamsPercentage.add(TeamPercentage(Team(2, "Prabowo-Sandi", ""), 47f, 200000))
        teamsPercentage.add(TeamPercentage(Team(2, "Tidak sah", ""), 6f, 200000))
        val data: MutableList<RekapitulasiData> = ArrayList()
        data.add(RekapitulasiData(100000, "1 mnt lalu", teamsPercentage, "Jawa Timur"))
        data.add(RekapitulasiData(100000, "1 mnt lalu", teamsPercentage, "Jawa Tengah"))
        view?.dismissLoading()
        view?.bindRekapitulasi(data)
    }
}