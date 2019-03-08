package com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.TPS

interface PerhitunganMainView : BaseView {
    fun bindTps(tps: TPS)
    fun showFailedDeleteTpsAlert()
    fun onSuccessDeleteTps()
}