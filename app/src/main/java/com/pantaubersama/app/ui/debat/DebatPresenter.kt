package com.pantaubersama.app.ui.debat

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.DebatInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Profile
import javax.inject.Inject

/**
 * @author edityomurti on 15/02/2019 12:31
 */
class DebatPresenter @Inject constructor(
    private val debatInteractor: DebatInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<DebatView>() {
    fun getMessage() {
        view?.showLoading()
        disposables.add(debatInteractor.getMessage()
            .subscribe(
                {
                    view?.dismissLoading()
                    view?.showMessage(it)
                },
                {
                }
            ))
    }

    fun getKomentar() {
        view?.showLoadingKomentar()
        disposables.add(debatInteractor.getKomentar()
            .subscribe(
                {
                    view?.dismissLoadingKomentar()
                    view?.showKomentar(it)
                },
                {
                }
            )
        )
    }

    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }
}