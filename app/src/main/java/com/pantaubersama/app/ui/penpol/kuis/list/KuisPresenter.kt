package com.pantaubersama.app.ui.penpol.kuis.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.Optional
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.optional
import io.reactivex.rxkotlin.Singles
import javax.inject.Inject

class KuisPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<KuisView>() {

    fun getTopPageItems() {
        view?.showLoading()
        val disposable = Singles.zip(
            bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_KUIS).optional(),
            kuisInteractor.getKuisUserSummary().optional(),
            kuisInteractor.getKuisList(1)
        )
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({ (banner, kuisResult, kuisList) ->
                val itemModels = mutableListOf<ItemModel>()
                if (banner is Optional.Some) itemModels += banner.value
                if (kuisResult is Optional.Some) itemModels += kuisResult.value
                itemModels += kuisList
                view?.showTopPageItems(itemModels)
            }, {
                view?.showError(it)
                view?.showFailedGetData()
            })
        disposables?.add(disposable)
    }
}