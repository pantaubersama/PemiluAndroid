package com.pantaubersama.app.ui.penpol.kuis.result

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class KuisResultPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor,
    private val profileInteractor: ProfileInteractor
): BasePresenter<KuisResultView>() {

    fun getKuisResult(kuisId: String) {
        view?.showLoading()
        val disposable = kuisInteractor.getKuisResult(kuisId)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                view?.showResult(it, profileInteractor.getProfile().name)
            }, {
                view?.showError(it)
            })
        disposables?.add(disposable)
    }
}