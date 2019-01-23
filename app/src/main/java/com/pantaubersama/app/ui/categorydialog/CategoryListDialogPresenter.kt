package com.pantaubersama.app.ui.categorydialog

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 23/01/2019 10:51
 */
class CategoryListDialogPresenter @Inject constructor(
    private val clusterInteractor: ClusterInteractor
) : BasePresenter<CategoryListDialogView>() {
    var perPage = 20

    fun getCategories(page: Int, query: String) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(
            clusterInteractor.getCategories(page, perPage, query)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it.data.categories.size != 0) {
                                view?.showCategory(it.data.categories)
                            } else {
                                view?.onEmptyData()
                            }
                        } else {
                            view?.showMoreCategory(it.data.categories)
                        }
                    },
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            view?.onFailedGetCategory()
                        } else {
                            view?.onFailedGetMoreCategory()
                        }
                        it.printStackTrace()
                        view?.showError(it)
                    }
                )
        )
    }
}