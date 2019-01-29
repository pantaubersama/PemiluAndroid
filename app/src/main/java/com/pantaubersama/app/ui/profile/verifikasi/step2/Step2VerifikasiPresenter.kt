package com.pantaubersama.app.ui.profile.verifikasi.step2

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import io.reactivex.rxkotlin.plusAssign
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class Step2VerifikasiPresenter @Inject constructor(
    private val verifikasiInteractor: VerifikasiInteractor
) : BasePresenter<Step2VerifikasiView>() {

    fun submitKtpPhoto(file: File) {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val formData = MultipartBody.Part.createFormData("ktp_photo", file.name, reqFile)

        view?.showLoading()
        disposables += verifikasiInteractor.submitKtpPhoto(formData)
            .subscribe({
                view?.dismissLoading()
                view?.onSuccessSubmitKtpPhoto()
            }, {
                view?.showError(it)
                view?.dismissLoading()
                view?.showFailedSubmitKtpPhotoAlert()
            })
    }
}