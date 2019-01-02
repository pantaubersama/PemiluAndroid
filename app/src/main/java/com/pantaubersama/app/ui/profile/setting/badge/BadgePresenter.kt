package com.pantaubersama.app.ui.profile.setting.badge

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class BadgePresenter @Inject constructor(
    private val interactor: ProfileInteractor
) : BasePresenter<BadgeView>() {

    fun refreshBadges() {
        view?.showLoading()
        val disposable = interactor.getBadges()
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe({
                    view?.showBadges(it)
                }, {
                    view?.showError(it)
                })
        disposables?.add(disposable)
    }
}