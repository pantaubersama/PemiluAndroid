package com.pantaubersama.app.ui.wordstadium.challenge.open

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.User
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.parseDate
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import java.util.*
import javax.inject.Inject

class PromoteChallengePresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor,
    private val loginInteractor: LoginInteractor
) : BasePresenter<PromoteChallengeView>() {
    fun getUserProfile() {
        view?.showProfile(profileInteractor.getProfile())
    }

    fun openChallenge(
        topicList: String?,
        statement: String?,
        statementSource: String?,
        showTimeAt: String?,
        timeLimit: Int
    ) {
        view?.showLoading()
        disposables.add(
                wordStadiumInteractor.openChallenge(
                        topicList,
                        statement,
                        statementSource,
                        showTimeAt?.parseDate(
                            toFormat = "dd-MM-yyyy HH:mm",
                            fromFormat = "dd-MM-yyyy HH:mm",
                            toTimeZone = TimeZone.getTimeZone("GMT"),
                            fromTimeZone = TimeZone.getDefault()),
                        timeLimit
                )
                        .subscribe(
                                {
                                    view?.dismissLoading()
                                    view?.onSuccessOpenChallenge()
                                },
                                {
                                    view?.dismissLoading()
                                    view?.showError(it)
                                }
                        )
        )
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
}