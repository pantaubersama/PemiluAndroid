package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TPSInteractor
import com.pantaubersama.app.data.model.tps.image.ImageLocalModel
import javax.inject.Inject

class UploadDocumentPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) : BasePresenter<UploadDocumentView>() {
    fun saveImages(
        tpsId: String,
        presiden: MutableList<ImageLocalModel>,
        dpr: MutableList<ImageLocalModel>,
        dpd: MutableList<ImageLocalModel>,
        dprdProv: MutableList<ImageLocalModel>,
        dprdKab: MutableList<ImageLocalModel>,
        suasanaTps: MutableList<ImageLocalModel>
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