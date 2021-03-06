package com.pantaubersama.app.ui.penpol.kuis.result

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class KuisUserResultPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<KuisUserResultView>() {
    fun getKuisUserResult() {
        view?.showLoading()
        val disposable = kuisInteractor.getKuisUserSummary()
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe({
                    view?.showKuisUserResult(it, profileInteractor.getProfile().fullName ?: "")
                }, {
                    view?.showError(it)
                })
        disposables.add(disposable)
    }

    fun getKuisUserResultByUserId(userId: String) {
        view?.showLoading()
        disposables.add(
            kuisInteractor.getKuisUserSummaryByUserId(userId)
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe(
                    {
                        view?.showKuisUserResult(it, it.user.fullName!!)
                    },
                    {
                        view?.showError(it)
                    }
                )
        )
    }
}