package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.RekapitulasiInteractor
import com.pantaubersama.app.data.model.rekapitulasi.EMPTY_PERCENTAGE
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
            rekapitulasiInteractor.getRekapitulasiList("nasional")
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.bindRekapitulasiList(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showFailedLoadRekapitulasiList()
                    }
                )
        )
    }

    fun getRekapitulasiNasional() {
        disposables.add(
            rekapitulasiInteractor.getRekapitulasiNasional()
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe(
                    {
                        if (it == EMPTY_PERCENTAGE) {
                            view?.showDataEmpty()
                        } else {
                            view?.bindRekapitulasiNasional(it)
                        }
                    },
                    {
                        view?.showError(it)
                        view?.showFailedGetRekapitulasiNasionalAlert()
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