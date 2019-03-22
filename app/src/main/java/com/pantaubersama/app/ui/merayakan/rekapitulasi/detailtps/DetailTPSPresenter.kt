package com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.RekapitulasiInteractor
import javax.inject.Inject

class DetailTPSPresenter @Inject constructor(
    private val rekapitulasiInteractor: RekapitulasiInteractor
) : BasePresenter<DetailTPSView>() {
    fun getRekapitulasi(tpsId: String, villageCode: Long, tpsNumber: Int) {
        view?.dismissLoading()
        disposables.add(
            rekapitulasiInteractor.getRekapitulasiDetail(tpsId, villageCode, tpsNumber)
                .subscribe(
                    {
                        view?.bindRealCount(it)
                        loadImages(tpsId, "c1_presiden")
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetRealCountAlert()
                    }
                )
        )
    }

    private fun loadImages(tpsId: String, imagesType: String) {
        disposables.add(
            rekapitulasiInteractor.getImages(tpsId, imagesType)
                .subscribe(
                    {
                        view?.bindImages(it)
                        loadC1Summary(tpsId, "presiden")
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetImagesAlert()
                    }
                )
        )
    }

    private fun loadC1Summary(tpsId: String, c1Type: String) {
        disposables.add(
            rekapitulasiInteractor.getC1Summary(tpsId, c1Type)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.bindC1Summary(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetC1SummaryAlert()
                    }
                )
        )
    }
}