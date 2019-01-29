package com.pantaubersama.app.ui.profile.setting

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.user.VerificationStep
import com.twitter.sdk.android.core.models.User

interface SettingView : BaseView {
    fun goToLogin()
    fun onSuccessGetProfile(profile: Profile)
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
    fun onSuccessRevokeFirebaseToken()
    fun showLogoutFailedAlert()
    fun showVerifikasiScreen(step: VerificationStep)
    fun showFailedGetVerifikasi()
}