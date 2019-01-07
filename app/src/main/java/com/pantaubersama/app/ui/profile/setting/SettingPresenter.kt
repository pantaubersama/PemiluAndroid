package com.pantaubersama.app.ui.profile.setting

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import javax.inject.Inject

class SettingPresenter @Inject constructor(private val loginInteractor: LoginInteractor?) : BasePresenter<SettingView>() {
    fun logOut(clientId: String?, clientSecret: String?) {
        disposables?.add(
                loginInteractor?.logOut(
                        clientId,
                        clientSecret,
                        loginInteractor.getToken()
                )?.doOnComplete {
                    loginInteractor.clearDataCache()
                    view?.goToLogin()
                }?.doOnError {
                    view?.showError(it)
                }!!.subscribe()
        )
    }
}