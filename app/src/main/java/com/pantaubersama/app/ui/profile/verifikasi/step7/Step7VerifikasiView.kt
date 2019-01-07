package com.pantaubersama.app.ui.profile.verifikasi.step7

import com.pantaubersama.app.base.BaseView

interface Step7VerifikasiView : BaseView {
    fun onSuccessSignature()
    fun showFailedSubmitSignatureAlert()
}