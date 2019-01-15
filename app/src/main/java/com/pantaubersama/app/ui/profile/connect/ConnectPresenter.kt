package com.pantaubersama.app.ui.profile.connect

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.utils.PantauConstants
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.User
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

    fun disconnectSocialMedia(accountType: String) {
        view?.showLoading()
        disposables.add(
            loginInteractor.disconnectSocielMedia(accountType)
                .subscribe(
                    {
                        view?.dismissLoading()
                        if (accountType == PantauConstants.CONNECT.FACEBOOK) {
                            view?.showSuccessDisconnectFacebookAlert()
                            view?.logoutFacebookSDK()
                        } else if (accountType == PantauConstants.CONNECT.TWITTER) {
                            view?.showSuccessDisconnectTwitterAlert()
                            view?.logoutTwitterSDK()
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        if (accountType == PantauConstants.CONNECT.FACEBOOK) {
                            view?.showFailedDisconnectFacebookAlert()
                        } else if (accountType == PantauConstants.CONNECT.TWITTER) {
                            view?.showFailedDisconnectTwitterAlert()
                        }
                    }
                )
        )
    }
}
