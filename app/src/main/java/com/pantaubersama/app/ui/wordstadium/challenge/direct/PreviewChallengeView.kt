package com.pantaubersama.app.ui.wordstadium.challenge.direct

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile
import com.twitter.sdk.android.core.models.User

interface PreviewChallengeView : BaseView {
    fun showProfile(profile: Profile)
    fun onSuccessOpenChallenge()
    fun showConnectedToTwitterAlert()
    fun showFailedToConnectTwitterAlert()
    fun bindTwitterUserData(data: User?)
    fun showFailedGetUserDataAlert()
    fun showSuccessDisconnectTwitterAlert()
    fun showFailedDisconnectTwitterAlert()
    fun logoutTwitterSDK()
    fun onFailedConnectTwitter()
}