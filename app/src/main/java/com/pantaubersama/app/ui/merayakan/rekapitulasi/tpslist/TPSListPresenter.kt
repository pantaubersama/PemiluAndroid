package com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.tps.TPSData
import com.pantaubersama.app.data.model.user.EMPTY_INFORMANT
import com.pantaubersama.app.data.model.user.Profile
import javax.inject.Inject

class TPSListPresenter @Inject constructor() : BasePresenter<TPSListView>() {
    fun getTPSListData() {
        view?.showLoading()
        val profile1 = Profile("", "", "Suparti", "", "",
            false, false, null, 0, null, false,
            null, "", "", "", "",
            Image("https://m.media-amazon.com/images/M/MV5BODI1NTg2MzE0N15BMl5BanBnXkFtZTcwMTE5NDgxOA@@._V1_SY1000_CR0,0,666,1000_AL_.jpg"), EMPTY_INFORMANT)
        val tpses: MutableList<TPSData> = ArrayList()
        tpses.add(
            TPSData("TPS 001", "DIY", "Sleman", "Moyudan", "Sumberagung", 0, profile1)
        )
        tpses.add(
            TPSData("TPS 002", "DIY", "Sleman", "Moyudan", "Sumberagung", 1, profile1)
        )
        tpses.add(
            TPSData("TPS 003", "DIY", "Sleman", "Moyudan", "Sumberagung", 2, profile1)
        )
        view?.dismissLoading()
        view?.bindPerhitungan(tpses)
    }
}