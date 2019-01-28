package com.pantaubersama.app.ui.clusterdetail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import javax.inject.Inject

class ClusterDetailPresenter @Inject constructor(private val clusterInteractor: ClusterInteractor) : BasePresenter<ClusterDetailView>() {
    fun getClusterById(clusterId: String) {
        view?.showLoading()
        disposables.add(
            clusterInteractor.getClusterById(clusterId)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.bindClusterData(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetClusterAlert()
                    }
                )
        )
    }
}