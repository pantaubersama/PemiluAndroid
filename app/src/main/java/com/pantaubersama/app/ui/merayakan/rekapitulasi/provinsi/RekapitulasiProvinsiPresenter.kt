package com.pantaubersama.app.ui.merayakan.rekapitulasi.provinsi

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.kuis.Team
import com.pantaubersama.app.data.model.kuis.TeamPercentage
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData
import javax.inject.Inject

class RekapitulasiProvinsiPresenter @Inject constructor() : BasePresenter<RekapitulasiProvinsiView>() {
    fun getRekapitulasiData() {
        view?.showLoading()
        val teamsPercentage: MutableList<TeamPercentage> = ArrayList()
        teamsPercentage.add(TeamPercentage(Team(1, "Jokowi-Ma'ruf", ""), 47f, 200000))
        teamsPercentage.add(TeamPercentage(Team(2, "Prabowo-Sandi", ""), 47f, 200000))
        teamsPercentage.add(TeamPercentage(Team(2, "Tidak sah", ""), 6f, 200000))
        val data: MutableList<RekapitulasiData> = ArrayList()
        data.add(RekapitulasiData(50000, "1 mnt lalu", teamsPercentage, "Sleman"))
        data.add(RekapitulasiData(20000, "1 mnt lalu", teamsPercentage, "Gunungkidul"))
        data.add(RekapitulasiData(30000, "1 mnt lalu", teamsPercentage, "Kulonprogo"))
        view?.dismissLoading()
        view?.bindRekapitulasi(data)
    }
}