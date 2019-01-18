package com.pantaubersama.app.ui.profile.setting.badge.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class DetailBadgePresenter @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenter<DetailBadgeView>() {
    fun getBadge(achivedId: String) {
        disposables.add(profileInteractor.getAchievedBadgeByID(achivedId)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe(
                {
                    view?.showBadge(it)
                },
                {
                    view?.onFailedGetBadge(it)
                    view?.showError(it)
                }
            )
        )
    }
}