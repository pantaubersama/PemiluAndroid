package com.pantaubersama.app.ui.onboarding

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import javax.inject.Inject

class OnBoardingPresenter @Inject constructor(private val loginInteractor: LoginInteractor) : BasePresenter<OnBoardingView>() {
    fun setOnboardingComplete() {
        loginInteractor.setOnboardingComplete()
    }
}