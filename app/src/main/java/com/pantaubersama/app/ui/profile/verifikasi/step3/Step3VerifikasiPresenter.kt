package com.pantaubersama.app.ui.profile.verifikasi.step3

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import okhttp3.MultipartBody
import javax.inject.Inject

class Step3VerifikasiPresenter @Inject constructor(private val verifikasiInteractor: VerifikasiInteractor) : BasePresenter<Step3VerifikasiView>() {
    fun submitSelfieKtp(ktpSelfie: MultipartBody.Part?) {
        view?.showLoading()
        disposables?.add(
            verifikasiInteractor.submitSelfieKtp(ktpSelfie)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.onSuccessSubmitSelfieKtp()
                    },
                    {
                        view?.showError(it)
                        view?.dismissLoading()
                        view?.showFailedSubmitSelfieKtpAlert()
                    }
                )
        )
    }
}