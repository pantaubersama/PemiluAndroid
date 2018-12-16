package com.pantaubersama.app.ui.home

import com.pantaubersama.app.base.BasePresenter

/**
 * @author edityomurti on 17/12/2018 02:23
 */
class HomePresenter : BasePresenter<HomeView>() {
    fun updateUser() {
        view?.onSuccessLoadUser()
    }
}