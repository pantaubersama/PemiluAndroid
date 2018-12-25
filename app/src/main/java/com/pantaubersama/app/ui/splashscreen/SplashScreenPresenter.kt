package com.pantaubersama.app.ui.splashscreen

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import javax.inject.Inject

class SplashScreenPresenter @Inject constructor(private val loginInteractor: LoginInteractor?) : BasePresenter<SplashScreenView>() {
    fun getLoginState() {
        if (loginInteractor?.getLoginState()!!) {
            view?.goToHome()
        } else {
            view?.goToLogin()
        }
    }
}