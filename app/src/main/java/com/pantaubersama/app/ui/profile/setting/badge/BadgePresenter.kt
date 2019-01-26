package com.pantaubersama.app.ui.profile.setting.badge

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.UserInteractor
import javax.inject.Inject

class BadgePresenter @Inject constructor(
    private val interactor: ProfileInteractor,
    private val userInteractor: UserInteractor
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
        disposables.add(disposable)
    }

    fun getUserBadges(userId: String) {
        disposables.add(
            userInteractor.getBadges(userId)
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe({
                    view?.showBadges(it)
                }, {
                    view?.showError(it)
                })
        )
    }
}