package com.pantaubersama.app.ui.penpol.kuis.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.utils.Optional
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Kuis.Filter
import com.pantaubersama.app.utils.extensions.optional
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class KuisPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<KuisView>() {

    private val perPage = 3

    private var isRunning = false
    private var currentPage = 1
    private var currentFilter = ""
        set(value) {
            field = value
            currentPage = 1
        }

    val filter: String
        get() = kuisInteractor.getKuisFilter()

    fun getTopPageItems() {
        view?.showLoading()
        isRunning = true
        currentFilter = filter.takeIf { it != Filter.KUIS_ALL } ?: Filter.BELUM_SELESAI

        disposables += Singles.zip(
            bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_KUIS).optional(),
            kuisInteractor.getKuisUserSummary().optional(),
            kuisInteractor.getKuisList(currentPage, perPage, currentFilter)
        )
            .doOnEvent { _, _ ->
                view?.dismissLoading()
                isRunning = false
            }
            .subscribe({ (banner, kuisResult, kuisList) ->
                val itemModels = mutableListOf<ItemModel>()
                if (banner is Optional.Some) itemModels += banner.value
                if (kuisResult is Optional.Some && kuisResult.value.meta.finished != 0) itemModels += kuisResult.value
                itemModels += kuisList
                view?.showTopPageItems(itemModels)

                checkNextPageAvailable(kuisList)
            }, {
                view?.showError(it)
                view?.showFailedGetData()
            })
    }

    fun getNextPage() {
        if (isRunning) return

        view?.showLoadingMore()
        isRunning = true

        disposables += kuisInteractor.getKuisList(currentPage, perPage, currentFilter)
            .doOnEvent { _, _ ->
                view?.dismissLoadingMore()
                isRunning = false
            }
            .subscribe({
                view?.showMoreKuis(it)

                checkNextPageAvailable(it)
            }, {
                view?.showError(it)
            })
    }

    private fun checkNextPageAvailable(result: List<KuisItem>) {
        when {
            result.size == perPage -> currentPage++
            filter == Filter.KUIS_ALL -> {
                currentFilter = when (currentFilter) {
                    Filter.BELUM_SELESAI -> Filter.BELUM_DIIKUTI
                    Filter.BELUM_DIIKUTI -> Filter.SELESAI
                    else -> {
                        view?.setNoMoreItems()
                        return
                    }
                }
            }
            else -> view?.setNoMoreItems()
        }
    }
}