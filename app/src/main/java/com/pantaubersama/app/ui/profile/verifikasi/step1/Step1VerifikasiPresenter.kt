package com.pantaubersama.app.ui.profile.verifikasi.step1

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import io.reactivex.rxkotlin.plusAssign
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class Step1VerifikasiPresenter @Inject constructor(
    private val verifikasiInteractor: VerifikasiInteractor
) : BasePresenter<Step1VerifikasiView>() {

    fun submitSelfieKtp(file: File) {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val formData = MultipartBody.Part.createFormData("ktp_selfie", file.name, reqFile)

        view?.showLoading()
        disposables += verifikasiInteractor.submitSelfieKtp(formData)
            .subscribe({
                view?.dismissLoading()
                view?.onSuccessSubmitSelfieKtp()
            }, {
                view?.showError(it)
                view?.dismissLoading()
                view?.showFailedSubmitSelfieKtpAlert()
            })
    }
}