package com.pantaubersama.app.ui.profile.setting.ubahdatalapor

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Informant

interface UbahDataLaporView : BaseView {
    fun setDataLapor(informant: Informant?)
    fun showFailedGetDataLaporAlert()
    fun disableView()
    fun showSuccessUpdateDataLaporAlert()
    fun enableView()
    fun showFailedUpdateDataLaporAlert()
    fun finishActivity()
}