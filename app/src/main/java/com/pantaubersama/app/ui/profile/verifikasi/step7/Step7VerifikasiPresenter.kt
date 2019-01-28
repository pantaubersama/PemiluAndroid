package com.pantaubersama.app.ui.profile.verifikasi.step7

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import io.reactivex.rxkotlin.plusAssign
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class Step7VerifikasiPresenter @Inject constructor(
    private val verifikasiInteractor: VerifikasiInteractor
) : BasePresenter<Step7VerifikasiView>() {

    fun submitSignaturePhoto(file: File) {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val formData = MultipartBody.Part.createFormData("signature", file.name, reqFile)

        view?.showLoading()
        disposables += verifikasiInteractor.submitSignaturePhoto(formData)
                .subscribe({
                    view?.dismissLoading()
                    view?.onSuccessSignature()
                }, {
                    view?.showError(it)
                    view?.dismissLoading()
                    view?.showFailedSubmitSignatureAlert()
                })
    }
}