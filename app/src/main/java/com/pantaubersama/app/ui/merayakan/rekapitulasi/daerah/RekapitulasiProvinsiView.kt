package com.pantaubersama.app.ui.merayakan.rekapitulasi.daerah

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData

interface RekapitulasiProvinsiView : BaseView {
    fun bindRekapitulasi(data: MutableList<RekapitulasiData>)
}