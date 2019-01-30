package com.pantaubersama.app.ui.profile.verifikasi.step1

import com.pantaubersama.app.base.BaseView

interface Step1VerifikasiView : BaseView {
    fun onSuccessSubmitSelfieKtp()
    fun showFailedSubmitSelfieKtpAlert()
}