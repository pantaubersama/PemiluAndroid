package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TPSInteractor
import com.pantaubersama.app.data.model.tps.image.Image
import javax.inject.Inject

class UploadDocumentPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) : BasePresenter<UploadDocumentView>() {
    fun saveImages(
        tpsId: String,
        presiden: MutableList<Image>,
        dpr: MutableList<Image>,
        dpd: MutableList<Image>,
        dprdProv: MutableList<Image>,
        dprdKab: MutableList<Image>,
        suasanaTps: MutableList<Image>
    ) {
        disposables.add(
            tpsInteractor.saveImageDoc(
                tpsId,
                presiden,
                dpr,
                dpd,
                dprdProv,
                dprdKab,
                suasanaTps
            ).subscribe(
                {
                    view?.finishSection()
                },
                {
                    view?.showError(it)
                    view?.showFailedSaveImageAlert()
                }
            )
        )
    }

    fun getImagesSaved(tpsId: String) {
        tpsInteractor.getImages(tpsId)?.let {
            view?.bindImages(it)
        }
    }
}