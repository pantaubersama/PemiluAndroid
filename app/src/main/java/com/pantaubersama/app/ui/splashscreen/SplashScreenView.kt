package com.pantaubersama.app.ui.splashscreen

import com.pantaubersama.app.base.BaseView

interface SplashScreenView : BaseView {
    fun onForceUpdateAvailable()
    fun goToHome()
    fun goToLogin()
}