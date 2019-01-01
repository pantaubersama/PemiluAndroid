package com.pantaubersama.app.ui.profile.setting.editprofile

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.User

interface EditProfileView : BaseView {
    fun onSuccessLoadUser(profile: User)
}