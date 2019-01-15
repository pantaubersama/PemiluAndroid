package com.pantaubersama.app.ui.profile.setting

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject
import com.twitter.sdk.android.core.TwitterException
import com.google.gson.Gson
import android.R.attr.data
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.models.User


class SettingPresenter @Inject constructor(
    private val loginInteractor: LoginInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<SettingView>() {
    fun logOut(clientId: String?, clientSecret: String?) {
        disposables?.add(
                loginInteractor.logOut(
                        clientId,
                        clientSecret
                )?.doOnComplete {
                    loginInteractor.clearDataCache()
                    view?.goToLogin()
                }?.doOnError {
                    view?.showError(it)
                }!!.subscribe()
        )
    }

    fun getProfile() {
        view?.onSuccessGetProfile(profileInteractor.getProfile())
    }

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

    fun getTwitterUserData() {
        loginInteractor.getTwitterAccountService()
            .verifyCredentials(false, false, false)
            .enqueue(object : Callback<User>() {
                override fun success(result: Result<User>?) {
                    view?.bindTwitterUserData(result?.data)
                }

                override fun failure(exception: TwitterException?) {
                    view?.showError(exception as Throwable)
                    view?.showFailedGetUserDataAlert()
                }
            })
    }
}