package com.pantaubersama.app.ui.menjaga.lapor

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class LaporPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<LaporView>() {
    fun getProfile() {
        view?.bindProfile(profileInteractor.getProfile())
    }

    fun getBanner() {
        view?.showLoading()
        disposables.add(bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_LAPOR)
            .subscribe(
                {
                    view?.showBanner(it)
                },
                {
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedGetDataAlert()
                }
            )
        )
    }
}