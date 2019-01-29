package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface LoginView : BaseView {
    fun showLoginFailedAlert()
    fun onSuccessGetProfile(it: Profile?)
    fun onSuccessLogin()
}