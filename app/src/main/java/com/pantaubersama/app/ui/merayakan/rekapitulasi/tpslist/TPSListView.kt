package com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.TPS

interface TPSListView : BaseView {
    fun bindPerhitungan(tpses: MutableList<TPS>)
}