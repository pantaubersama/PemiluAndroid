package com.pantaubersama.app.ui.penpol.tanyakandidat.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import javax.inject.Inject

class FilterTanyaKandidatPresenter @Inject constructor(private val tanyaKandidatInteractor: TanyaKandidatInteractor) : BasePresenter<FilterTanyaKandidatView>() {
    fun saveTanyaKandidatFilter(userFilter: String?, orderFilter: String?) {
        disposables.add(
            tanyaKandidatInteractor.saveTanyaKandidatFilter(userFilter, orderFilter)
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

    fun loadTanyaKandidatUserFilter() {
        disposables.add(
            tanyaKandidatInteractor
                .loadTanyaKandidatUserFilter()
                .subscribe(
                    {
                        view?.setUserFilter(it)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedLoadUserfilter()
                    }
                )
        )
    }

    fun loadTanyaKandidatOrderFilter() {
        disposables.add(
            tanyaKandidatInteractor
                .loadTanyaKandidatOrderFilter()
                .subscribe(
                    {
                        view?.setOrderFilter(it)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedLoadOrderfilter()
                    }
                )
        )
    }

    fun loadTanyaKandidatUserFilterSearch() {
        disposables.add(
            tanyaKandidatInteractor
                .loadTanyaKandidatUserFilterSearch()
                .subscribe(
                    {
                        view?.setUserFilter(it)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedLoadUserfilter()
                    }
                )
        )
    }

    fun loadTanyaKandidatOrderFilterSearch() {
        disposables.add(
            tanyaKandidatInteractor
                .loadTanyaKandidatOrderFilterSearch()
                .subscribe(
                    {
                        view?.setOrderFilter(it)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedLoadOrderfilter()
                    }
                )
        )
    }

    fun saveTanyaKandidatFilterSaerch(userFilter: String?, orderFilter: String?) {
        disposables.add(
            tanyaKandidatInteractor.saveTanyaKandidatFilterSearch(userFilter, orderFilter)
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