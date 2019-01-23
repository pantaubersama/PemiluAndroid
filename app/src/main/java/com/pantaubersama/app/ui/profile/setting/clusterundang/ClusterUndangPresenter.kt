package com.pantaubersama.app.ui.profile.setting.clusterundang

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import javax.inject.Inject

class ClusterUndangPresenter @Inject constructor(
    private val clusterInteractor: ClusterInteractor
) : BasePresenter<ClusterUndangView>() {
    fun invite(email: String) {
        view?.showLoading()
        disposables.add(
            clusterInteractor
                .invite(email)
                .subscribe(
                    {
                        view?.finishThisSection()
                        view?.dismissLoading()
                        view?.showSuccessInviteAlert()
                    },
                    {
                        view?.showError(it)
                        view?.dismissLoading()
                        view?.showFailedInviteAlert()
                    }
                )
        )
    }
}