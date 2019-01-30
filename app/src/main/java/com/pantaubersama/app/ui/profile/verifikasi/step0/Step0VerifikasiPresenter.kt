package com.pantaubersama.app.ui.profile.verifikasi.step0

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import javax.inject.Inject

class Step0VerifikasiPresenter @Inject constructor(
    private val verifikasiInteractor: VerifikasiInteractor
) : BasePresenter<Step0VerifikasiView>() {
    fun submitKtpNumber(ktpNumber: String) {
        view?.showLoading()
        view?.disableView()
        disposables.add(
            verifikasiInteractor
                .submitKtpNumber(ktpNumber)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.onKtpNumberSubmited()
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedSubmitKtpNumberAlert()
                        view?.enableView()
                    }
                )
        )
    }
}