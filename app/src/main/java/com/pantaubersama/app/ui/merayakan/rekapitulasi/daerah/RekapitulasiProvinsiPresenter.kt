package com.pantaubersama.app.ui.merayakan.rekapitulasi.daerah

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RekapitulasiInteractor
import javax.inject.Inject

class RekapitulasiProvinsiPresenter @Inject constructor(
    private val rekapitulasiInteractor: RekapitulasiInteractor
) : BasePresenter<RekapitulasiProvinsiView>() {
    fun getRekapitulasiKabupatenData() {
//        view?.showLoading()
//        val teamsPercentage: MutableList<TeamPercentage> = ArrayList()
//        teamsPercentage.add(TeamPercentage(Team(1, "Jokowi-Ma'ruf", ""), 47f, 200000))
//        teamsPercentage.add(TeamPercentage(Team(2, "Prabowo-Sandi", ""), 47f, 200000))
//        teamsPercentage.add(TeamPercentage(Team(2, "Tidak sah", ""), 6f, 200000))
//        val data: MutableList<RekapitulasiData> = ArrayList()
//        data.add(RekapitulasiData(50000, "1 mnt lalu", teamsPercentage, "Sleman"))
//        data.add(RekapitulasiData(20000, "1 mnt lalu", teamsPercentage, "Gunungkidul"))
//        data.add(RekapitulasiData(30000, "1 mnt lalu", teamsPercentage, "Kulonprogo"))
//        view?.dismissLoading()
//        view?.bindRekapitulasi(data)
    }

    fun getRekapitulasiKecamatanData() {
//        view?.showLoading()
//        val teamsPercentage: MutableList<TeamPercentage> = ArrayList()
//        teamsPercentage.add(TeamPercentage(Team(1, "Jokowi-Ma'ruf", ""), 47f, 200000))
//        teamsPercentage.add(TeamPercentage(Team(2, "Prabowo-Sandi", ""), 47f, 200000))
//        teamsPercentage.add(TeamPercentage(Team(2, "Tidak sah", ""), 6f, 200000))
//        val data: MutableList<RekapitulasiData> = ArrayList()
//        data.add(RekapitulasiData(10000, "1 mnt lalu", teamsPercentage, "Godean"))
//        data.add(RekapitulasiData(5000, "1 mnt lalu", teamsPercentage, "Minggir"))
//        data.add(RekapitulasiData(2000, "1 mnt lalu", teamsPercentage, "Moyudan"))
//        view?.dismissLoading()
//        view?.bindRekapitulasi(data)
    }

    fun getRekapitulasiKelurahanData() {
//        view?.showLoading()
//        val teamsPercentage: MutableList<TeamPercentage> = ArrayList()
//        teamsPercentage.add(TeamPercentage(Team(1, "Jokowi-Ma'ruf", ""), 47f, 200000))
//        teamsPercentage.add(TeamPercentage(Team(2, "Prabowo-Sandi", ""), 47f, 200000))
//        teamsPercentage.add(TeamPercentage(Team(2, "Tidak sah", ""), 6f, 200000))
//        val data: MutableList<RekapitulasiData> = ArrayList()
//        data.add(RekapitulasiData(1000, "1 mnt lalu", teamsPercentage, "Sumberagung"))
//        data.add(RekapitulasiData(5000, "1 mnt lalu", teamsPercentage, "Sumberarum"))
//        data.add(RekapitulasiData(2000, "1 mnt lalu", teamsPercentage, "Sumberrahayu"))
//        data.add(RekapitulasiData(2000, "1 mnt lalu", teamsPercentage, "Sumbersari"))
//        view?.dismissLoading()
//        view?.bindRekapitulasi(data)
    }

    fun getRekapitulasi(parent: String, regionCode: Long) {
        view?.showLoading()
        disposables.add(
            rekapitulasiInteractor.getRekapitulasiList(parent, regionCode)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.bindRekapitulasi(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedLoadRekapitulasiAlert()
                    }
                )
        )
    }
}