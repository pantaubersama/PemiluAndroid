package com.pantaubersama.app.ui.profile

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface ProfileView : BaseView {
    fun showProfile(profile: Profile)
}