package com.pantaubersama.app.ui.profile.connect

import com.pantaubersama.app.base.BaseView

interface ConnectView : BaseView {
    fun showConnectedToFacebookAlert()
    fun showFailedToConnectFacebookAlert()
    fun showConnectedToTwitterAlert()
    fun showFailedToConnectTwitterAlert()
}