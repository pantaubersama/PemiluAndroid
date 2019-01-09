package com.pantaubersama.app.ui.profile.cluster.requestcluster

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import okhttp3.MultipartBody
import javax.inject.Inject

class RequestClusterPresenter @Inject constructor(private val clusterInteractor: ClusterInteractor) : BasePresenter<RequestClusterView>() {
    fun requestCluster(clusterName: String, categoryId: String, clusterDescription: String, image: MultipartBody.Part?) {
        view?.showLoading()
        view?.disableView()
        disposables?.add(
            clusterInteractor
                .requestCluster(clusterName, categoryId, clusterDescription, image)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.showSuccessRequestClusterAlert()
                        view?.onSuccessRequestAlert()
                    },
                    {
                        view?.enableView()
                        view?.showFailedRequestClusterAlert()
                        view?.dismissLoading()
                        view?.showError(it)
                    }
                )
        )
    }
}