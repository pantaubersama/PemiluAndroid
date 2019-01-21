package com.pantaubersama.app.ui.splashscreen

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashScreenPresenter @Inject constructor(private val loginInteractor: LoginInteractor) : BasePresenter<SplashScreenView>() {
    fun checkAppVersion(currentVersionCode: Int) {
        view?.showLoading()
        disposables.add(
            loginInteractor.isForceUpdateAvalable()
                .timeout(5000, TimeUnit.MILLISECONDS)
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe(
                    {
                        if (currentVersionCode < it.versionCode && it.isForceUpdate) {
                            view?.onForceUpdateAvailable()
                        } else {
                            getLoginState()
                        }
                    },
                    {
                        getLoginState()
                    }
                )

        )
    }

    private fun getLoginState() {
        if (loginInteractor.getLoginState()!!) {
            view?.goToHome()
        } else {
            view?.goToLogin()
        }
    }

    fun getOnboardingStatus() {
        view?.isOnboardingComplete(loginInteractor.getOnboardingStatus())
    }
}