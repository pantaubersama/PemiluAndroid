package com.pantaubersama.app.ui.profile.setting

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface SettingView : BaseView {
    fun goToLogin()
    fun onSuccessGetProfile(profile: Profile)
}