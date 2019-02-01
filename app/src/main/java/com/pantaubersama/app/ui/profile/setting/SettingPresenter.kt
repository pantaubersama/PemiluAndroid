package com.pantaubersama.app.ui.profile.setting

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.VerifikasiInteractor
import javax.inject.Inject
import com.twitter.sdk.android.core.TwitterException
import com.pantaubersama.app.utils.PantauConstants
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.models.User
import io.reactivex.rxkotlin.plusAssign

class SettingPresenter @Inject constructor(
    private val loginInteractor: LoginInteractor,
    private val profileInteractor: ProfileInteractor,
    private val verifikasiInteractor: VerifikasiInteractor
) : BasePresenter<SettingView>() {
    fun logOut(clientId: String?, clientSecret: String?) {
        disposables.add(
                loginInteractor.logOut(
                        clientId,
                        clientSecret
                ).subscribe(
                    {
                        loginInteractor.clearDataCache()
                        view?.goToLogin()
                    },
                    {
                        view?.showError(it)
                        view?.showLogoutFailedAlert()
                    }
                )
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
                        view?.onFailedConnectFacebook()
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
                        view?.onFailedConnectTwitter()
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
            loginInteractor.disconnectSocialMedia(accountType)
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

    fun revokeFirebaseToken() {
        disposables.add(
            loginInteractor.updateFirebaseToken("")
                .subscribe(
                    {
                        view?.onSuccessRevokeFirebaseToken()
                    },
                    {
                        view?.showError(it)
                        view?.showLogoutFailedAlert()
                    }
                )
        )
    }

    fun getStatusVerifikasi() {
        view?.showLoading()
        disposables += verifikasiInteractor.getStatusVerifikasi()
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showVerifikasiScreen(it)
            }, {
                view?.showError(it)
                view?.showFailedGetVerifikasi()
            })
    }
}