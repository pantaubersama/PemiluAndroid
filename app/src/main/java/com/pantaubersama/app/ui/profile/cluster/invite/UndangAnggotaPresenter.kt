package com.pantaubersama.app.ui.profile.cluster.invite

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class UndangAnggotaPresenter @Inject constructor(
    private val clusterInteractor: ClusterInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<UndangAnggotaView>() {
    fun invite(email: String) {
        view?.showLoading()
        view?.disableView()
        disposables.add(
            clusterInteractor
                .invite(email, profileInteractor.getProfile().cluster?.id!!)
                .subscribe(
                    {
                        view?.enableView()
                        view?.removeEmail()
                        view?.dismissLoading()
                        view?.showSuccessInviteAlert()
                    },
                    {
                        view?.enableView()
                        view?.showError(it)
                        view?.dismissLoading()
                        view?.showFailedInviteAlert()
                    }
                )
        )
    }

    fun enableDisableUrlInvite(clusterId: String, disable: Boolean) {
        disposables.add(
            clusterInteractor.enableDisableUlInvite(clusterId, disable)
                .subscribe(
                    {
                        if (disable) {
                            view?.showSuccessEnableUrlInviteAlert()
                        } else {
                            view?.showSuccessDisableUrlInviteAlert()
                        }
                    },
                    {
                        if (disable) {
                            view?.showFailedEnableUrlInviteAlert()
                        } else {
                            view?.showFailedDisableUrlInviteAlert()
                        }
                        view?.showError(it)
                        view?.reverseView(!disable)
                    }
                )
        )
    }
}