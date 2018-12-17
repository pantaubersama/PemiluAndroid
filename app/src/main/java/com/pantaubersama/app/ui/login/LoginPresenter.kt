package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.RxSchedulers

class LoginPresenter(private val rxSchedulers: RxSchedulers?) : BasePresenter<LoginView>() {

    fun exchangeToken(oAuthToken: String?, registrationId: String?) {
        view?.showLoading()
        // invoke data manager
    }
}