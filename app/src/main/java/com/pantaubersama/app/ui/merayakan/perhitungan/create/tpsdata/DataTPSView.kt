package com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.Province

interface DataTPSView : BaseView {
    fun showProvincesLoading()
    fun dismissProvincesLoading()
    fun showFailedGetProvincesAlert()
    fun bindProvincesToSpinner(provinces: MutableList<Province>)
}