package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val loginInteractor: LoginInteractor?) : BasePresenter<LoginView>() {

    fun exchangeToken(oAuthToken: String?, registrationId: String?) {
        view?.showLoading()
        loginInteractor?.exchangeToken(oAuthToken)
            ?.doOnSuccess { token ->
                loginInteractor.saveLoginData(token)
                view?.dismissLoading()
                view?.openHomeActivity()
            }
            ?.doOnError { e ->
                view?.dismissLoading()
                view?.showError(e!!)
                view?.showLoginFailedAlert()
            }
            ?.subscribe()
    }
}