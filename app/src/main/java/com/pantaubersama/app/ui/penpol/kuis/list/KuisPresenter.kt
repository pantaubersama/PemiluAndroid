package com.pantaubersama.app.ui.penpol.kuis.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class KuisPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor?,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<KuisView>() {
    fun getList() {
        view?.showLoading()
        disposables?.add(bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_KUIS)
            ?.subscribe(
                {
                    view?.showBanner(it)
                },
                {
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedGetData()
                }
            )!!
        )
    }
}