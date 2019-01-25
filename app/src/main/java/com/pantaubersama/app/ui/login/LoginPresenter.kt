package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val loginInteractor: LoginInteractor?,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<LoginView>() {

    fun exchangeToken(oAuthToken: String?, firebaseToken: String?) {
        view?.showLoading()
        val disposable = loginInteractor?.exchangeToken(oAuthToken, firebaseToken)
            ?.subscribe({
                loginInteractor.saveLoginData(it.token)
                loginInteractor.saveFirebaseToken(firebaseToken)
                getProfile()
            }, {
                view?.dismissLoading()
                view?.showError(it!!)
                view?.showLoginFailedAlert()
            })
        disposables.add(disposable!!)
    }

    private fun getProfile() {
        val disposable = profileInteractor.refreshProfile().subscribe(
                {
                    view?.dismissLoading()
                    view?.onSuccessGetProfile(it)
                },
                {
                    view?.dismissLoading()
                    view?.showError(it!!)
                    view?.showLoginFailedAlert()
                }
            )
        disposables.add(disposable)
    }
}