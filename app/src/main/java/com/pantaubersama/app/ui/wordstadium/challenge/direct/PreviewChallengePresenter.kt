package com.pantaubersama.app.ui.wordstadium.challenge.direct

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.utils.PantauConstants
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.User
import javax.inject.Inject

class PreviewChallengePresenter @Inject constructor(
        private val profileInteractor: ProfileInteractor,
        private val wordStadiumInteractor: WordStadiumInteractor,
        private val loginInteractor: LoginInteractor
) : BasePresenter<PreviewChallengeView>() {

    fun getUserProfile() {
        view?.showProfile(profileInteractor.getProfile())
    }

    fun directChallenge(
            topicList: String?,
            statement: String?,
            statementSource: String?,
            showTimeAt: String?,
            timeLimit: Int,
            invitationId: String?,
            screenName: String?
    ) {
        view?.showLoading()
        disposables.add(
                wordStadiumInteractor.directChallenge(
                        topicList,
                        statement,
                        statementSource,
                        showTimeAt,
                        timeLimit,
                        invitationId,
                        screenName
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
                                    if (accountType == PantauConstants.CONNECT.TWITTER) {
                                        view?.showSuccessDisconnectTwitterAlert()
                                        view?.logoutTwitterSDK()
                                    }
                                },
                                {
                                    view?.dismissLoading()
                                    view?.showError(it)
                                    if (accountType == PantauConstants.CONNECT.TWITTER) {
                                        view?.showFailedDisconnectTwitterAlert()
                                    }
                                }
                        )
        )
    }
}