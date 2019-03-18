package com.pantaubersama.app.ui.note

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.PartyInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.partai.PoliticalParty
import javax.inject.Inject

class CatatanPilihanPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val partyInteractor: PartyInteractor
) : BasePresenter<CatatanPilihanView>() {
    fun submitCatatanku(paslonSelected: Int, partySelected: PoliticalParty) {
        view?.showLoading()
        disposables.add(
                profileInteractor.submitCatatanku(paslonSelected, partySelected)
                        .subscribe(
                                {
                                    view?.dismissLoading()
                                    view?.showSuccessSubmitCatatanAlert()
                                    view?.finishActivity()
                                },
                                {
                                    view?.dismissLoading()
                                    view?.showFailedSubmitCatatanAlert()
                                    view?.showError(it)
                                }
                        )
        )
    }

    fun getUserProfile() {
        view?.bindUserProfile(profileInteractor.getProfile())
    }

    fun getMyTendency() {
        disposables.add(
            profileInteractor.getMyTendency()
                .subscribe(
                    {
                        view?.bindMyTendency(it, profileInteractor.getProfile().name)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedGetMyTendencyAlert()
                    }
                )
        )
    }

    fun getPartai(page: Int, perPage: Int) {
        view?.showPartiesProgressBar()
        disposables.add(
            partyInteractor.getParties(page, perPage)
                .subscribe(
                    {
                        view?.dismissPartiesProgressBar()
                        view?.showPartai(it)
                    },
                    {
                        view?.dismissPartiesProgressBar()
                        view?.showError(it)
                        view?.showFailedGetPartiesAlert()
                    }
                )
        )
    }
}