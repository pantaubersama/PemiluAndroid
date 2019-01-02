package com.pantaubersama.app.ui.profile

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.utils.State
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor
) : BasePresenter<ProfileView>() {

    fun getProfile() {
        view?.showProfile(profileInteractor.getProfile())
    }

    fun refreshProfile() {
        view?.showLoading()
        val disposable = profileInteractor.refreshProfile()
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe({
                    view?.showProfile(it)
                }, {
                    view?.showError(it)
                })
        disposables?.add(disposable)
    }

    fun refreshBadges() {
        view?.showBadges(State.Loading)
        val disposable = profileInteractor.getBadges()
                .subscribe({
                    view?.showBadges(State.Success, it.take(3))
                }, {
                    view?.showBadges(State.Error(it.message))
                })
        disposables?.add(disposable)
    }
}