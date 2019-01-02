package com.pantaubersama.app.ui.profile.setting.editprofile

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor

class EditProfilePresenter(private val profileInteractor: ProfileInteractor) : BasePresenter<EditProfileView>() {
    fun updateUser() {
        view?.onSuccessLoadUser(profileInteractor.getProfile())
    }
}