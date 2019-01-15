package com.pantaubersama.app.ui.profile.connect

import com.pantaubersama.app.base.BaseView
import com.twitter.sdk.android.core.models.User

interface ConnectView : BaseView {
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
}