package com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.* // ktlint-disable

interface DataTPSView : BaseView {
    fun showProvincesLoading()
    fun dismissProvincesLoading()
    fun showFailedGetProvincesAlert()
    fun bindProvincesToSpinner(provinces: MutableList<Province>)
    fun showRegenciesLoading()
    fun dismissRegenciesLoading()
    fun showFailedGetRegenciesAlert()
    fun bindRegenciesToSpinner(regencies: MutableList<Regency>)
    fun showDistrictsLoading()
    fun dismissDistrictsLoading()
    fun showFailedGetDistrictsAlert()
    fun bindDistrictsToSpinner(districts: MutableList<District>)
    fun showVillagesLoading()
    fun dismissVillagesLoading()
    fun bindVillagesToSpinner(villages: MutableList<Village>)
    fun showFailedGetVillagesAlert()
    fun onSuccessSaveTPS(tps: TPS)
    fun failedSaveTpsAlert()
    fun failedUpdateTpsAlert()
    fun onSuccessUpdateTPS()
}