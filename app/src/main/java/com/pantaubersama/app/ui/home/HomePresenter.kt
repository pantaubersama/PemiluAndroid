package com.pantaubersama.app.ui.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor

/**
 * @author edityomurti on 17/12/2018 02:23
 */
class HomePresenter(private val profileInteractor: ProfileInteractor) : BasePresenter<HomeView>() {
    fun updateUser() {
        view?.onSuccessLoadUser(profileInteractor.getProfile())
    }
}