package com.pantaubersama.app.ui.penpol.kuis.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import javax.inject.Inject

class KuisPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor?
) : BasePresenter<KuisView>() {
    fun isBannerShown() {
        if (!kuisInteractor?.isBannerShown()!!) {
            view?.showBanner()
        }
    }
}