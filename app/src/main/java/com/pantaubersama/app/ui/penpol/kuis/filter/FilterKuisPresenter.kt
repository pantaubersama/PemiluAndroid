package com.pantaubersama.app.ui.penpol.kuis.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import javax.inject.Inject

class FilterKuisPresenter @Inject constructor(private val kuisInteractor: KuisInteractor) : BasePresenter<FilterKuisView>() {
    fun getKuisFilterData() {
        view?.setKuisFilter(kuisInteractor.getKuisFilter())
    }

    fun saveKuisFilter(kuisFilter: String?) {
        disposables?.add(
            kuisInteractor
                .saveKuisFilter(kuisFilter)
                .subscribe(
                    {
                        view?.onSuccessSaveKuisFilter()
                        view?.showSuccessSaveKuisFilterAlert()
                    },
                    {
                        view?.showError(it)
                        view?.showFailedSaveKuisFilterAlert()
                    }
                )
        )
    }

}