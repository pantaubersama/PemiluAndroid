package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val loginInteractor: LoginInteractor?) : BasePresenter<LoginView>() {

    fun exchangeToken(oAuthToken: String?, registrationId: String?) {
        view?.showLoading()
        val disposable: Disposable?
        disposable = loginInteractor?.exchangeToken(oAuthToken)
            ?.subscribe({
                loginInteractor.saveLoginData(it.token)
                view?.dismissLoading()
                view?.openHomeActivity()
            },{
                view?.dismissLoading()
                view?.showError(it!!)
                view?.showLoginFailedAlert()
            })
        disposables?.add(disposable!!)
    }
}