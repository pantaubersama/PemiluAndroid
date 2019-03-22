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
                        view?.dismissLoading()
                        view?.bindImages(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetImagesAlert()
                    }
                )
        )
    }
}