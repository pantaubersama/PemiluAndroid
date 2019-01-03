package com.pantaubersama.app.ui.profile.setting.editprofile

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface EditProfileView : BaseView {
    fun onSuccessLoadUser(profile: Profile)
    fun showFailedGetUserDataAlert()
    fun showProfileUpdatedAlert()
    fun showFailedUpdateProfileAlert()
    fun disableView()
    fun finishThisScetion()
    fun enableView()
    fun refreshProfile()
    fun showSuccessUpdateAvatarAlert()
    fun showFailedUpdateAvatarAlert()
}