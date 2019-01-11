package com.pantaubersama.app.ui.penpol.kuis.result

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class KuisSummaryPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor
) : BasePresenter<KuisSummaryView>() {

    fun getKuisSummary(kuisId: String) {
        view?.showLoading()
        disposables += kuisInteractor.getKuisSummary(kuisId)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showAnswers(it)
            }, {
                view?.showError(it)
            })
    }
}