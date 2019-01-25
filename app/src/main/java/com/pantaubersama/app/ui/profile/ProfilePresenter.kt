package com.pantaubersama.app.ui.profile

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.UserInteractor
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.State
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val userInteractor: UserInteractor
) : BasePresenter<ProfileView>() {

    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

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
        disposables.add(disposable)
    }

    fun refreshBadges() {
        view?.showBadges(State.Loading)
        val disposable = profileInteractor.getBadges()
                .subscribe({
                    view?.showBadges(State.Success, it.take(3))
                }, {
                    view?.showBadges(State.Error(it.message))
                })
        disposables.add(disposable)
    }

    fun leaveCluster(name: String?) {
        disposables.add(
            profileInteractor.leaveCluster()
                .subscribe(
                    {
                        view?.showSuccessLeaveClusterAlert(name)
                        view?.showRequestClusterLayout()
                        view?.showProfile(profileInteractor.getProfile())
                    },
                    {
                        view?.showError(it)
                        view?.showFailedLeaveClusterAlert(name)
                    }
                )
        )
    }

    fun getUserProfile(userId: String) {
        disposables.add(
            userInteractor.getUserProfile(userId)
                .subscribe({
                    view?.showProfile(it)
                }, {
                    view?.showError(it)
                    view?.showFailedGetProfileAlert()
                })
        )
    }

    fun getUserBadge(userId: String) {
        view?.showBadges(State.Loading)
        val disposable = userInteractor.getBadges(userId)
            .subscribe({
                view?.showBadges(State.Success, it.take(3))
            }, {
                view?.showBadges(State.Error(it.message))
            })
        disposables.add(disposable)
    }
}