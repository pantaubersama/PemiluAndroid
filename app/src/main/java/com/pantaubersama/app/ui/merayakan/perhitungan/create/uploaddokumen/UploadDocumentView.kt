package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.image.Image
import com.pantaubersama.app.data.model.tps.image.ImageDoc

interface UploadDocumentView : BaseView {
    fun showFailedSaveImageAlert()
    fun finishSection()
    abstract fun bindImages(images: ImageDoc)
    fun bindImagesFromApi(images: MutableList<Image>)
    fun showFailedGetImagesAlert()
}