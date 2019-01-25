package com.pantaubersama.app.ui.profile.cluster.category

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
                        view?.onSuccessAddNewCategory(it.data.category)
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

    fun getCategories(page: Int, perPage: Int, query: String) {
        if (page == 1) {
            view?.showLoading()
        }
        disposables.add(
            clusterInteractor
                .getCategories(
                    page,
                    perPage,
                    query
                )
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it.data.categories.size != 0) {
                                view?.bindData(it.data.categories)
                                if (it.data.categories.size < perPage) {
                                    view?.setDataEnd()
                                }
                            } else {
                                view?.showEmptyAlert()
                            }
                        } else {
                            if (it.data.categories.size != 0) {
                                view?.bindNextData(it.data.categories)
                                if (it.data.categories.size < perPage) {
                                    view?.setDataEnd()
                                }
                            } else {
                                view?.setDataEnd()
                            }
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetCategoriesAlert()
                    }
                )
        )
    }
}