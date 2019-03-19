package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import com.pantaubersama.app.base.BaseView

interface UploadDocumentView : BaseView {
    fun showFailedSaveImageAlert()
    fun finishSection()
}