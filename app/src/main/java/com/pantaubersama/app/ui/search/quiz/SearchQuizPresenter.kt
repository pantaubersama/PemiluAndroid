package com.pantaubersama.app.ui.search.quiz

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.utils.PantauConstants.Kuis.Filter
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class SearchQuizPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<SearchQuizView>() {

    private val perPage = 5

    private var isRunning = false
    private var currentPage = 1
    private var currentFilter = ""
        set(value) {
            field = value
            currentPage = 1
        }

    val isLoggedIn: Boolean
        get() = profileInteractor.getProfile() != EMPTY_PROFILE

    val filter: String
        get() = kuisInteractor.getKuisFilter().takeIf { isLoggedIn }
            ?: Filter.BELUM_DIIKUTI

    fun getFirstPage(keyword: String) {
        view?.showLoading()

        isRunning = true
        currentFilter = filter.takeIf { it != Filter.KUIS_ALL }
            ?: Filter.BELUM_SELESAI

        disposables.add(
            kuisInteractor.getKuisList(currentPage, perPage, currentFilter, keyword)
                .doOnEvent { _, _ ->
                    view?.dismissLoading()
                    isRunning = false
                }
                .subscribe({
                    if (it.isEmpty()) {
                        view?.showEmptyData()
                    } else {
                        view?.showFirstPage(it)
                    }

                    checkNextPageAvailable(it, keyword)
                }, {
                    view?.showFailedGetData()
                    view?.showError(it)
                })
        )
    }

    fun getNextPage(keyword: String) {
        if (isRunning) return

        view?.showLoadingMore()
        isRunning = true

        disposables += kuisInteractor.getKuisList(currentPage, perPage, currentFilter, keyword)
            .doOnEvent { _, _ ->
                view?.dismissLoadingMore()
                isRunning = false
            }
            .subscribe({
                view?.showMoreData(it)

                checkNextPageAvailable(it, keyword)
            }, {
                view?.showFailedGetMoreData()
                view?.showError(it)
            })
    }

    private fun checkNextPageAvailable(result: List<KuisItem>, keyword: String) {
        when {
            result.size == perPage -> currentPage++
            filter == Filter.KUIS_ALL -> when (currentFilter) {
                Filter.BELUM_SELESAI -> {
                    currentFilter = Filter.BELUM_DIIKUTI
                    if (result.size < perPage) getNextPage(keyword)
                }
                Filter.BELUM_DIIKUTI -> {
                    currentFilter = Filter.SELESAI
                    if (result.size < perPage) getNextPage(keyword)
                }
                else -> view?.setNoMoreData()
            }
            else -> view?.setNoMoreData()
        }
    }
}