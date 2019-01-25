package com.pantaubersama.app.ui.profile.verifikasi.step7

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import okhttp3.MultipartBody
import javax.inject.Inject

class Step7VerifikasiPresenter @Inject constructor(private val verifikasiInteractor: VerifikasiInteractor) : BasePresenter<Step7VerifikasiView>() {
    fun submitSignaturePhoto(signPhoto: MultipartBody.Part?) {
        view?.showLoading()
        disposables?.add(
            verifikasiInteractor.submitSignaturePhoto(signPhoto)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.onSuccessSignature()
                    },
                    {
                        view?.showError(it)
                        view?.dismissLoading()
                        view?.showFailedSubmitSignatureAlert()
                    }
                )
        )
    }
}