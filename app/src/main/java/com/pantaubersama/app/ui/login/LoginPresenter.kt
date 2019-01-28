package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val loginInteractor: LoginInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<LoginView>() {

    fun exchangeToken(oAuthToken: String?) {
        view?.showLoading()
        disposables.add(
            loginInteractor.exchangeToken(oAuthToken)
            .subscribe({
                loginInteractor.saveLoginData(it.token)
                view?.onSuccessLogin()
                getProfile()
            }, {
                view?.dismissLoading()
                view?.showError(it)
                view?.showLoginFailedAlert()
            }))
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

    fun saveFirebaseKey(firebaseToken: String) {
        disposables.add(
            loginInteractor.updateFirebaseToken(firebaseToken)
                .subscribe(
                    {
                        getProfile()
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it!!)
                        view?.showLoginFailedAlert()
                    }
                )
        )
    }
}