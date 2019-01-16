package com.pantaubersama.app.ui.note.presiden

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class PaslonPresenter @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenter<PaslonView>() {
    fun getUserProfile() {
        view?.bindUserProfile(profileInteractor.getProfile())
    }

}