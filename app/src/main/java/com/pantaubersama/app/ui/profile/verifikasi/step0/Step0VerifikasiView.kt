package com.pantaubersama.app.ui.profile.verifikasi.step0

import com.pantaubersama.app.base.BaseView

interface Step0VerifikasiView : BaseView {
    fun onKtpNumberSubmited()
    fun showFailedSubmitKtpNumberAlert()
    fun disableView()
    fun enableView()
}