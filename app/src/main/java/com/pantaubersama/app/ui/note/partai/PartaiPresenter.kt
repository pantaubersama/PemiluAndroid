package com.pantaubersama.app.ui.note.partai

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class PartaiPresenter @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenter<PartaiView>() {
    fun getUserProfile() {
        view?.bindUserProfile(profileInteractor.getProfile())
    }

    fun getPartai(page: Int, perPage: Int) {
        disposables.add(
                profileInteractor.getPartai(page, perPage)
                        .subscribe(
                                {
                                    view?.showPartai(it)
                                },
                                {
                                    view?.showError(it)
                                }
                        )
        )
    }
}