package com.pantaubersama.app.ui.profile.setting.ubahsandi

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor

class UbahSandiPresenter(private val profileInteractor: ProfileInteractor) : BasePresenter<UbahSandiView>() {
    fun updatePassword(password: String, confirmation: String) {
        view?.showLoading()
        view?.disableView()
        disposables?.add(profileInteractor
            .updatePassword(
                password,
                confirmation
            )
            .subscribe(
                {
                    view?.dismissLoading()
                    view?.showSuccessUpdatePasswordAlert()
                    view?.finishActivity()
                },
                {
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedUpdatePasswordAlert()
                    view?.enableView()
                }
            )
        )
    }
}