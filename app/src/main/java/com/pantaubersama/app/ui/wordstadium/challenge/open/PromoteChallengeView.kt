package com.pantaubersama.app.ui.wordstadium.challenge.open

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile
import com.twitter.sdk.android.core.models.User

interface PromoteChallengeView : BaseView {
    fun showProfile(profile: Profile)
    fun onSuccessOpenChallenge()
    fun showConnectedToFacebookAlert()
    fun showFailedToConnectFacebookAlert()
    fun showConnectedToTwitterAlert()
    fun showFailedToConnectTwitterAlert()
    fun bindTwitterUserData(data: User?)
    fun showFailedGetUserDataAlert()
    fun showSuccessDisconnectFacebookAlert()
    fun showSuccessDisconnectTwitterAlert()
    fun showFailedDisconnectFacebookAlert()
    fun showFailedDisconnectTwitterAlert()
    fun logoutFacebookSDK()
    fun logoutTwitterSDK()
    fun onFailedConnectFacebook()
    fun onFailedConnectTwitter()
}