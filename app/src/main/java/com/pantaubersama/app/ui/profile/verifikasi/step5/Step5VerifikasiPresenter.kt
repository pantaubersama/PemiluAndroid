package com.pantaubersama.app.ui.profile.verifikasi.step5

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import okhttp3.MultipartBody
import javax.inject.Inject

class Step5VerifikasiPresenter @Inject constructor(private val verifikasiInteractor: VerifikasiInteractor) : BasePresenter<Step5VerifikasiView>() {
    fun submitKtpPhoto(ktpPhoto: MultipartBody.Part?) {
        view?.showLoading()
        disposables?.add(
            verifikasiInteractor.submitKtpPhoto(ktpPhoto)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.onSuccessSubmitKtpPhoto()
                    },
                    {
                        view?.showError(it)
                        view?.dismissLoading()
                        view?.showFailedSubmitKtpPhotoAlert()
                    }
                )
        )
    }
}