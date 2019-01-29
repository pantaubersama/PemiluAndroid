package com.pantaubersama.app.ui.menjaga.filter.partiesdialog

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.PartyInteractor
import javax.inject.Inject

class PartiesDialogPresenter @Inject constructor(private val partyInteractor: PartyInteractor) : BasePresenter<PartiesDialogView>() {
    fun getParties() {
        view?.showLoading()
        disposables.add(
            partyInteractor.getParties(1, 100)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.showPartai(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                    }
                )
        )
    }
}