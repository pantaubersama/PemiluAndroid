package com.pantaubersama.app.ui.profile.setting.editprofile

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import okhttp3.MultipartBody
import javax.inject.Inject

class EditProfilePresenter @Inject constructor(
        private val profileInteractor: ProfileInteractor
) : BasePresenter<EditProfileView>() {

    fun getUserData() {
        view?.onSuccessLoadUser(profileInteractor.getProfile())
    }

    fun refreshUserData() {
        disposables?.add(
            profileInteractor.refreshProfile()
            .subscribe(
                {
                    view?.onSuccessLoadUser(it)
                },
                {
                    view?.showError(it)
                    view?.showFailedGetUserDataAlert()
                }
            )
        )
    }

    fun saveEditedUserData(
        name: String?,
        username: String?,
        location: String?,
        description: String?,
        education: String?,
        occupation: String?
    ) {
        view?.showLoading()
        view?.disableView()
        disposables?.add(
            profileInteractor.updateUserData(
                name,
                username,
                location,
                description,
                education,
                occupation
            )
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.showProfileUpdatedAlert()
                        view?.finishThisScetion()
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedUpdateProfileAlert()
                        view?.enableView()
                    }
                )
        )
    }

    fun uploadAvatar(avatar: MultipartBody.Part?) {
        disposables?.add(
            profileInteractor.uploadAvatar(avatar)
                .subscribe(
                    {
                        view?.refreshProfile()
                        view?.showSuccessUpdateAvatarAlert()
                    },
                    {
                        view?.showFailedUpdateAvatarAlert()
                        view?.showError(it)
                    }
                )
        )
    }
}