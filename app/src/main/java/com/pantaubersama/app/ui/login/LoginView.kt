package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BaseView

interface LoginView : BaseView {
    fun openHomeActivity()
    fun showLoginFailedAlert()
}