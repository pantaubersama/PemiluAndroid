package com.pantaubersama.app.ui.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 17/12/2018 02:23
 */
class HomePresenter @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenter<HomeView>() {
    fun updateUser() {
        view?.onSuccessLoadUser(profileInteractor.getProfile())
    }
}