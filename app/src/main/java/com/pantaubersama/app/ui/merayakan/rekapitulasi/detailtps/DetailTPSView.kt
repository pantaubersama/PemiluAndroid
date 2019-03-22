package com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.rekapitulasi.Percentage

interface DetailTPSView : BaseView {
    fun showFailedGetRealCountAlert()
    fun bindRealCount(rekapitulasi: Percentage)
}