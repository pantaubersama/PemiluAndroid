package com.pantaubersama.app.ui.merayakan.perhitungan.create.c1

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.C1Form

interface C1FormView : BaseView {
    fun bindC1Data(c1Form: C1Form)
    fun onSuccessSaveData()
    fun showFailedSaveDataAlert()
}