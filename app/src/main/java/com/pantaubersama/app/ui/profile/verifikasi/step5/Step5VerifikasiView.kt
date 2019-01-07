package com.pantaubersama.app.ui.profile.verifikasi.step5

import com.pantaubersama.app.base.BaseView

interface Step5VerifikasiView : BaseView {
    fun onSuccessSubmitKtpPhoto()
    fun showFailedSubmitKtpPhotoAlert()
}