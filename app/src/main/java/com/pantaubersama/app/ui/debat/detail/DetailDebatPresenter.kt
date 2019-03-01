package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Profile
import javax.inject.Inject

/**
 * @author edityomurti on 01/03/2019 18:05
 */

class DetailDebatPresenter @Inject constructor(
    val profileInteractor: ProfileInteractor
) : BasePresenter<DetailDebatView>() {
    fun getMyProfile() : Profile {
        return profileInteractor.getProfile()
    }
}