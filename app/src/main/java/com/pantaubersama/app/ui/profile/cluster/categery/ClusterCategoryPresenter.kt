package com.pantaubersama.app.ui.profile.cluster.categery

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import javax.inject.Inject

class ClusterCategoryPresenter @Inject constructor(private val clusterInteractor: ClusterInteractor) : BasePresenter<ClusterCategoryView>() {
    fun createNewCategory(categoryName: String) {
        view?.showAddCategoryLoading()
        view?.disableAddCategoryView()
        disposables?.add(
            clusterInteractor
                .createNewCategory(categoryName)
                .subscribe(
                    {
                        view?.onSuccessAddNewCategory()
                    },
                    {
                        view?.dismissAddCategoryLoading()
                        view?.showError(it)
                        view?.enableAddCategoryView()
                        view?.showFailedAddCategoryAlert()
                    }
                )
        )
    }
}