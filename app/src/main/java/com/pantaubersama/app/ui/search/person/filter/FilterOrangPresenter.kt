package com.pantaubersama.app.ui.search.person.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.UserInteractor
import javax.inject.Inject

class FilterOrangPresenter @Inject constructor(private val userInteractor: UserInteractor) : BasePresenter<FilterOrangView>() {
    fun loadUserFilter() {
        view?.setFilter(userInteractor.getSearchOrangFilter())
    }

    fun saveSearchOrangFilter(searchOrangFilter: String) {
        disposables.add(
            userInteractor.saveSearchOrangFilter(searchOrangFilter)
                .subscribe(
                    {
                        view?.onSuccessSaveFilter()
                        view?.showSuccessSaveFilterAlert()
                    },
                    {
                        view?.showError(it)
                        view?.showFailedSaveFilterAlert()
                    }
                )
        )
    }
}