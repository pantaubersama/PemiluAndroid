package com.pantaubersama.app.ui.debat

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.DebatInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 15/02/2019 12:31
 */
class DebatPresenter @Inject constructor(
    private val debatInteractor: DebatInteractor
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
}