package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.RekapitulasiInteractor
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class RekapitulasiPresenter @Inject constructor(
    private val bannerInfoInteractor: BannerInfoInteractor,
    private val rekapitulasiInteractor: RekapitulasiInteractor
) : BasePresenter<RekapitulasiView>() {
    fun getBanner() {
        view?.showLoading()
        disposables.add(
            bannerInfoInteractor.getBannerInfo(PantauConstants.REKAPITULASI)
                .subscribe(
                    {
                        view?.showBanner(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetBannerAlert()
                    }
                )
        )
    }

    fun getRekapitulasiData() {
        disposables.add(
            rekapitulasiInteractor.getRekapitulasiList()
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.bindRekapitulasiList(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedLoadRekapitulasiList()
                    }
                )
        )
    }

    fun getRekapitulasiNasional() {
        disposables.add(
            rekapitulasiInteractor.getRekapitulasiNasional()
                .subscribe(
                    {
                        view?.bindRekapitulasiNasional(it)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedGetRekapitulasiNasionalAlert()
                        view?.dismissLoading()
                    }
                )
        )
    }

    fun getTotalParticipant() {
        disposables.add(
            rekapitulasiInteractor.getTotalParticipant()
                .subscribe(
                    {
                        view?.bindTotalParticipantData(it)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedGetTotalParticipantAlert()
                        view?.dismissLoading()
                    }
                )
        )
    }
}