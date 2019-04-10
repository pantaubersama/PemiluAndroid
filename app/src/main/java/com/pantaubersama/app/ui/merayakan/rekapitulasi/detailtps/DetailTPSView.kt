package com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.tps.c1.C1Form
import com.pantaubersama.app.data.model.tps.image.Image

interface DetailTPSView : BaseView {
    fun showFailedGetRealCountAlert()
    fun bindRealCount(rekapitulasi: Percentage)
    fun showFailedGetImagesAlert()
    fun bindImages(images: MutableList<Image>)
    fun showFailedGetC1SummaryAlert(message: String?)
    fun bindC1Summary(c1Form: C1Form)
}