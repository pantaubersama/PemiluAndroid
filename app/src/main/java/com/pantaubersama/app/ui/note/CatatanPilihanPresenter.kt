package com.pantaubersama.app.ui.note

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class CatatanPilihanPresenter @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenter<CatatanPilihanView>() {
    fun submitCatatanku(paslonSelected: Int) {
        view?.showLoading()
        disposables.add(
            profileInteractor.submitCatatanku(paslonSelected)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.showSuccessSubmitCatatanAlert()
                        view?.finishActivity()
                    },
                    {
                        view?.dismissLoading()
                        view?.showFailedSubmitCatatanAlert()
                        view?.showError(it)
                    }
                )
        )
    }
}