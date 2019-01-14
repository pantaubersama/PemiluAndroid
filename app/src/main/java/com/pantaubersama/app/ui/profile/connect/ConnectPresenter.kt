package com.pantaubersama.app.ui.profile.connect

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import javax.inject.Inject

class ConnectPresenter @Inject constructor(private val loginInteractor: LoginInteractor) : BasePresenter<ConnectView>() {
    fun connectFacebook(accountType: String, token: String?) {
        view?.showLoading()
        disposables.add(
            loginInteractor
                .connectFacebook(accountType, token)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.showConnectedToFacebookAlert()
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedToConnectFacebookAlert()
                    }
                )
        )
    }

    fun connectTwitter(accountType: String, token: String, secret: String) {
        view?.showLoading()
        disposables.add(
            loginInteractor
                .connectTwitter(
                    accountType,
                    token,
                    secret
                )
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.showConnectedToTwitterAlert()
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedToConnectTwitterAlert()
                    }
                )
        )
    }
}