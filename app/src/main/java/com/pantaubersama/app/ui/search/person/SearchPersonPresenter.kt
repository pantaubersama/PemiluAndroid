package com.pantaubersama.app.ui.search.person

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.UserInteractor
import javax.inject.Inject

class SearchPersonPresenter @Inject constructor(private val userInteractor: UserInteractor) : BasePresenter<SearchPersonView>() {
    fun searchPerson(keyword: String, page: Int, perPage: Int) {
        view?.showLoading()
        disposables.add(
            userInteractor.searchPerson(keyword, page, perPage)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it.size != 0) {
                                view?.bindData(it)
                                if (it.size < perPage) {
                                    view?.setDataEnd()
                                }
                            } else {
                                view?.showEmptyData()
                            }
                        } else {
                            view?.bindMoreData(it)
                            if (it.size < perPage) {
                                view?.setDataEnd()
                            }
                        }
                    },
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            view?.showFailedGetDataAlert()
                        } else {
                            view?.showFailedGetMoreDataAlert()
                        }
                        it.printStackTrace()
                        view?.showError(it)
                    }
                )
        )
    }
}