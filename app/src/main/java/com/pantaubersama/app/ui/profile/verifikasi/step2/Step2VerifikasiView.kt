package com.pantaubersama.app.ui.profile.verifikasi.step2

import com.pantaubersama.app.base.BaseView

interface Step2VerifikasiView : BaseView {
    fun onSuccessSubmitKtpPhoto()
    fun showFailedSubmitKtpPhotoAlert()
}